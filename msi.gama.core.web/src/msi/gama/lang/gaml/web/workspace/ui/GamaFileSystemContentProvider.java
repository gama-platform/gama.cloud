/**
 * <copyright>
 *
 * Copyright (c) 2015 PlugBee. All rights reserved.
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Amine Lajmi - Initial API and implementation
 *
 * </copyright>
 */
package msi.gama.lang.gaml.web.workspace.ui;

import java.util.ArrayList;

import org.dslforge.workspace.ui.FileSystemContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GamaFileSystemContentProvider implements ITreeContentProvider {

	private static final long serialVersionUID = 1L;

	private java.io.File rootDirectory;

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof java.io.File) {
			rootDirectory = (java.io.File) inputElement;
			return getChildren(rootDirectory);
		}
		return null;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof File) {
			rootDirectory = (java.io.File) newInput;
		}
	}

	public static Drive drive;

	/**
	 * @param parentElement
	 * @return Object[]
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		GoogleCredential credential = (GoogleCredential) RWT.getApplicationContext().getAttribute("credential");
		if (credential != null) {
			String token = credential.getAccessToken();
			TokenResponse tokenResponse = new TokenResponse();
			tokenResponse.setAccessToken(token);
			tokenResponse.setTokenType("bearer");
			tokenResponse.setScope(DriveScopes.DRIVE_FILE);
			// GoogleCredential credential = new
			// GoogleCredential.Builder().setJsonFactory(JSON_FACTORY)
			// .setTransport(TRANSPORT).setClientSecrets(clientSecrets).build().setFromTokenResponse(tokenResponse);

			try {

				// Use access token to call API
				drive = new Drive.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
						.setApplicationName("GAMA Cloud").build();

				String pageToken = null;
				ArrayList<GDriveFile> lf = new ArrayList();
				do {
					FileList result = drive.files().list().setSpaces("drive")
							.setQ("'root' in parents and trashed = false")

							.setPageToken(pageToken).execute();

					for (File file : result.getItems()) {
						GDriveFile gf = new GDriveFile(file.getId(), file.getTitle());
						gf.downloadUrl = file.getDownloadUrl();
						if ("application/vnd.google-apps.folder".equals(file.getMimeType())) {
							gf.isDir = true;
						}
						lf.add(gf);
						// if("application/vnd.google-apps.folder".equals(file.getMimeType())) {
						// System.out.printf(">>Folder ");
						// }
						// System.out.printf(" %s \n",file.getTitle());

					}
					pageToken = result.getNextPageToken();
					// System.out.printf("---------pageToken %s \n",pageToken);

				} while (pageToken != null);
				return lf.toArray();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			if (parentElement instanceof File) {
				java.io.File file = (java.io.File) parentElement;
				if (file.isDirectory()) {
					java.io.File[] f = file.listFiles();
					return f;
				}
			}
		}
		return new Object[] {};
	}

	/**
	 * @param element
	 * @return Object
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof File) {
			File file = (File) element;
			// if (file.getParent() != null)
			// return new File(file.getParent());
			if (file.getParents().size() > 0) {
				return file.getParents();
			}
		}
		return null;
	}

	/**
	 * @param element
	 * @return boolean
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof GDriveFile) {
			GDriveFile file = (GDriveFile) element;
			if (file.isDirectory()) {
				// if (file.list().length > 0)
				return true;
			}
		}
		return false;
	}
}
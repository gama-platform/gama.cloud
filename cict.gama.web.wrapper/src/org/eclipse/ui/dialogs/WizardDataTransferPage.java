package org.eclipse.ui.dialogs;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group; 
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;

public class WizardDataTransferPage extends WizardPage {

	protected WizardDataTransferPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	public void displayErrorDialog(String zipimportBadformat) {
		// TODO Auto-generated method stub
		System.out.println(zipimportBadformat);
	}

	public WizardDataTransferPage(String pageName) {
		super(pageName);
	}

	public void initializeDialogUnits(Composite parent) {
		// TODO Auto-generated method stub
		
	}

	public void createOptionsGroup(Composite workArea) {
		// TODO Auto-generated method stub
		
	}
	public void setControl(Composite workArea) {
		// TODO Auto-generated method stub
		
	} 

	public void setPageComplete(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void setMessage(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setTitle(String string) {
		// TODO Auto-generated method stub
		
	}

	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Control getControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void performHelp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setImageDescriptor(ImageDescriptor image) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canFlipToNextPage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizardPage getNextPage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizardPage getPreviousPage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizard getWizard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPageComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPreviousPage(IWizardPage page) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWizard(IWizard newWizard) {
		// TODO Auto-generated method stub
		
	}

	protected void createOptionsGroupButtons(Group optionsGroup) {
		// TODO Auto-generated method stub
		
	}

	public void restoreWidgetValues() {
		// TODO Auto-generated method stub
		
	}

	public void saveWidgetValues() {
		// TODO Auto-generated method stub
		
	}


	public String[] addToHistory(String[] sourceNames, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	protected boolean allowNewContainerName() {
		// TODO Auto-generated method stub
		return false;
	} 
}

package msi.gama.lang.gaml.web.ui.factories;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.ui.IWorkbenchPage;

import msi.gama.common.interfaces.IConsoleDisplayer;
import msi.gama.common.interfaces.IGamaView;
import msi.gama.common.interfaces.IGui;
import msi.gama.common.interfaces.IGamaView.Console;
import msi.gama.kernel.experiment.ITopLevelAgent;
import msi.gama.lang.gaml.web.editor.GAMAHelper;
import msi.gama.lang.gaml.web.ui.utils.WorkbenchHelper;
import msi.gama.util.GamaColor;
import msi.gaml.operators.Strings;

public class ConsoleDisplayer implements IConsoleDisplayer {

	private final StringBuilder consoleBuffer = new StringBuilder(2000);
	String uid="";
	public ConsoleDisplayer(String u) {
		// TODO Auto-generated constructor stub
		uid=u;
	}

	@Override
	public void debugConsole(final int cycle, final String msg, final ITopLevelAgent root) {
		this.debugConsole(cycle, msg, root, null);
	}

	@Override
	public void debugConsole(final int cycle, final String msg, final ITopLevelAgent root, final GamaColor color) {
		writeToConsole("(cycle : " + cycle + ") " + msg + Strings.LN, root, color);
	}

	@Override
	public void informConsole(final String msg, final ITopLevelAgent root) {
		this.informConsole(msg, root, null);
	}

	@Override
	public void informConsole(final String msg, final ITopLevelAgent root, final GamaColor color) {
		writeToConsole(msg + Strings.LN, root, color);
	}

	private void writeToConsole(final String msg, final ITopLevelAgent root, final GamaColor color) {
//		final String uid=RWT.getUISession().getAttribute("user").toString();
		final String uid=WorkbenchHelper.UISession.get(root.getExperiment().getSpecies().getExperimentScope());

		final IGamaView.Console console = (Console) WorkbenchHelper.findView(uid,IGui.CONSOLE_VIEW_ID, null, true);
		if (console != null) {
			console.append(msg, root, color);
		} else {
			consoleBuffer.append(msg);
		}
	}

	@Override
	public void eraseConsole(final boolean setToNull) {
		final String uid=RWT.getUISession().getAttribute("user").toString();
		final IGamaView console = (IGamaView) WorkbenchHelper.findView(uid,IGui.CONSOLE_VIEW_ID, null, false);
		if (console != null) {
			WorkbenchHelper.run(uid,() -> console.reset());
		}
	}

	@Override
	public void showConsoleView(final ITopLevelAgent agent) {
		final IGamaView.Console icv = (Console) GAMAHelper.getGui().showView(agent.getScope(), IGui.INTERACTIVE_CONSOLE_VIEW_ID,
				null, IWorkbenchPage.VIEW_VISIBLE);
		if (icv != null)
			icv.append(null, agent, null);
		final IGamaView.Console console =
				(Console) GAMAHelper.getGui().showView(agent.getScope(), IGui.CONSOLE_VIEW_ID, null, IWorkbenchPage.VIEW_VISIBLE);
		if (consoleBuffer.length() > 0 && console != null) {
			console.append(consoleBuffer.toString(), agent, null);
			consoleBuffer.setLength(0);
		}

	}
}


package swtlaunch.entrypoints

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

class AppActionBarAdvisor(configurer : IActionBarConfigurer) extends ActionBarAdvisor(configurer) {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
//	var exitAction : IWorkbenchAction = _;

	override def makeActions(window : IWorkbenchWindow) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.

//		exitAction = ActionFactory.QUIT.create(window);
//		register(exitAction);
	}

	override def fillMenuBar(menuBar : IMenuManager) {
//		val fileMenu = new MenuManager("&File",
//				IWorkbenchActionConstants.M_FILE);
//		menuBar.add(fileMenu);
//		fileMenu.add(exitAction);
	}

}

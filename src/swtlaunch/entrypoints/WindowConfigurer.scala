package swtlaunch.entrypoints
import org.eclipse.ui.application.IWorkbenchWindowConfigurer
import org.eclipse.swt.graphics.Point

class WindowConfigurer {
	def configureWorkbenchWindow(configurer : IWorkbenchWindowConfigurer) : Unit = {
		configurer.setInitialSize(new Point(600, 850));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setTitle("Bar!!");

	}
}
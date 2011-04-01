package swtlaunch.entrypoints
import org.eclipse.swt.widgets.Composite
import org.gitwave.main.TopLevel

class LaunchView (parent : Composite){
	val tl = new TopLevel(parent);
	
	def setFocus() {
		tl.setFocus()
	}
}
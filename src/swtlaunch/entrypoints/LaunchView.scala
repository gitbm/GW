package swtlaunch.entrypoints
import org.eclipse.swt.widgets.Composite
import org.gitwave.main.TopLevel

class LaunchView (parent : Composite, style : Int){
	new TopLevel(parent, style);
}
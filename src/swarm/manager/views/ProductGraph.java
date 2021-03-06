package swarm.manager.views;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import swarm.core.domain.Project;
import swarm.core.server.SwarmServer;
import swarm.core.util.WorkbenchUtil;

public class ProductGraph extends ViewPart {

	public static final String ID = "swarm.manager.views.ProductGraph";

	private Browser browser;

	protected Project project;
	
	public void createPartControl(Composite parent) {
		browser = new Browser(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		String url = SwarmServer.getInstance().getServerUrl() +  "graphByProduct.html?productId=1";
		System.out.println("Global URL: " + url);
		browser.setUrl(url);
		browser.refresh();
		
		browser.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				//browser.refresh();
				Object jsvar = browser.evaluate("return tapped;");
				if(jsvar != null) {
					String typeFullName = jsvar.toString().substring(1);
					try {
						IJavaProject javaProject = WorkbenchUtil.getProjectByName("JabRef3.2");
						IType javaType = javaProject.findType(typeFullName);
						WorkbenchUtil.openEditor(javaType);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}				
			}
		});
		
		browser.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent event) {

			}
			
			@Override
			public void keyPressed(KeyEvent event) {
				switch (event.keyCode) {
				case SWT.F5:
					browser.refresh();
					break;
				default:
					break;
				}
			}
		});
		
		browser.addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent arg0) {
				browser.refresh();
			}
			
			@Override
			public void controlMoved(ControlEvent arg0) {}
		});
		
		browser.addStatusTextListener(new StatusTextListener() {
			@Override
			public void changed(StatusTextEvent event) {
				if (event.text.startsWith("MOUSEDOWN: ")) {
					System.out.println(event.text);
				}
			}
		});
	}
	
	public void setProject(Project project) {
		this.project = project;
	}

	public void setFocus() {
		browser.setFocus();
	}
}
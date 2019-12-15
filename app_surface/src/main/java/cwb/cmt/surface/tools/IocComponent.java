package cwb.cmt.surface.tools;

import  cwb.cmt.surface.tools.ContextualComponent;
import  cwb.cmt.surface.tools.Ioc;

public abstract class IocComponent extends ContextualComponent implements Ioc {
	
	public abstract boolean componentExists(String name);
	
	public abstract <T> T getComponent(String name);
	
	public  abstract <T> T getComponent(String name, Object... args);	
}

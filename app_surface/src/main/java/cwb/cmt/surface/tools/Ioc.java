package cwb.cmt.surface.tools;

public interface Ioc {
	
	public boolean componentExists(String name);
	
	public <T> T getComponent(String name);

	public <T> T getComponent(String name, Object... args);	
}

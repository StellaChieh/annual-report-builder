package cwb.cmt.surface.tools;

public interface ContextLoadingMonitor {	
	void addOnContextLoadingCompleteListener(OnContextLoadingCompleteListener listener);
	boolean removeOnContextLoadingCompleteListener(OnContextLoadingCompleteListener listener);
}

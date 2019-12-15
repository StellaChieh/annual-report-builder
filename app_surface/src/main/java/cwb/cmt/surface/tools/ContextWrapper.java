package cwb.cmt.surface.tools;


public abstract class ContextWrapper<ContextType> implements Context {
	
	private Object mBase;
	
	public ContextWrapper(ContextType context) {
		mBase = context;
	}
	
	public void attachBaseContext(ContextType rawContext) {
       if (mBase != null) {
            throw new IllegalStateException("Base context already set.");
        }
        mBase = rawContext;
	}
	
    public void updateBaseContext(ContextType rawContext) {
        mBase = rawContext;
    }
	
	@SuppressWarnings("unchecked")
	public ContextType getRawContext() {
		return (ContextType) mBase;
	}
}

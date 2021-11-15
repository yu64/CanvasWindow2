package canvas2.core;

@FunctionalInterface
public interface Updatable extends AppObject{

	public void update(float tpf) throws Exception;
}

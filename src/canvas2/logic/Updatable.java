package canvas2.logic;

import canvas2.AppObject;

@FunctionalInterface
public interface Updatable extends AppObject{

	public void update(float tpf) throws Exception;
}

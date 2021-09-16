package main.logic;

import main.AppObject;

@FunctionalInterface
public interface Updatable extends AppObject{

	public void update(float tpf) throws Exception;
}

package canvas2.core;

import java.awt.Graphics2D;

import canvas2.view.scene.Node;

/**
 * 描画可能なもの。関数型インターフェイスとして使える。<br>
 * 主に{@link Node}などで用いる。
 *
 */
@FunctionalInterface
public interface Drawable extends AppObject{

	public void draw(Graphics2D g2);
}

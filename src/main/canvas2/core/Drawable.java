package canvas2.core;

import java.awt.Graphics2D;

/**
 * 描画可能なもの。関数型インターフェイスとして使える。<br>
 */
@FunctionalInterface
public interface Drawable extends AppObject{

	public void draw(Graphics2D g2);
}

package canvas2.util;

import java.awt.Graphics;

/**
 * Graphicsの描画を簡略化するユーティリティクラス。
 *
 */
public final class GraphicsUtil {

	private GraphicsUtil(){

	}

	/**
	 * グリッドを描画する
	 * @param g {@link Graphics}
	 * @param x 左上の横座標
	 * @param y 左上の縦座標
	 * @param w 横幅
	 * @param h 縦幅
	 * @param size マスのサイズ
	 */
	public static void drawGrid(Graphics g, int x, int y, int w, int h, int size)
	{
		int minX = x;
		int minY = y;

		int maxX = x + w;
		int maxY = y + h;

		for(int i = minX; i <= maxX; i += size)
		{
			g.drawLine(i, minY, i, maxY);
		}

		for(int i = minY; i <= maxY; i += size)
		{
			g.drawLine(minX, i, maxX, i);
		}
	}


}

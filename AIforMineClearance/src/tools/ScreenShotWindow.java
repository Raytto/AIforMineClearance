package tools;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JWindow;

public class ScreenShotWindow extends JWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5857352758286534251L;
	private int orgx, orgy, endx, endy;
	private BufferedImage image = null;
	private BufferedImage tempImage = null;
	private BufferedImage saveImage = null;
	private Rectangle selectedRectangle = null;
	

	public Rectangle getSelectedRectangle() {
		return selectedRectangle;
	}

	public ScreenShotWindow() throws AWTException {
		// ��ȡ��Ļ�ߴ�
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, d.width, d.height);

		// ��ȡ��Ļ
		Robot robot = new Robot();
		image = robot
				.createScreenCapture(new Rectangle(0, 0, d.width, d.height));

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				orgx = e.getXOnScreen();
				orgy = e.getYOnScreen();
				System.out.println("Pressed:" + orgx + " " + orgy);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				endx = e.getXOnScreen();
				endy = e.getYOnScreen();
				System.out.println("Relesed:" + endx + " " + endy);
				selectedRectangle=new Rectangle(orgx, orgy, endx-orgx, endy-orgy);
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// ����϶�ʱ����¼���겢�ػ洰��
				endx = e.getX();
				endy = e.getY();

				// ��ʱͼ�����ڻ�����Ļ���������Ļ��˸
				Image tempImage2 = createImage(
						ScreenShotWindow.this.getWidth(),
						ScreenShotWindow.this.getHeight());
				Graphics g = tempImage2.getGraphics();
				g.drawImage(tempImage, 0, 0, null);
				int x = Math.min(orgx, endx);
				int y = Math.min(orgy, endy);
				int width = Math.abs(endx - orgx) + 1;
				int height = Math.abs(endy - orgy) + 1;
				// ����1��ֹwidth��height0
				g.setColor(Color.BLUE);
				g.drawRect(x - 1, y - 1, width + 1, height + 1);
				// ��1��1���˷�ֹͼƬ���ο򸲸ǵ�
				saveImage = image.getSubimage(x, y, width, height);
				g.drawImage(saveImage, x, y, null);

				ScreenShotWindow.this.getGraphics().drawImage(tempImage2, 0, 0,
						ScreenShotWindow.this);
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		RescaleOp ro = new RescaleOp(1f, 0, null);
		tempImage = ro.filter(image, null);
		g.drawImage(tempImage, 0, 0, this);
	}
}
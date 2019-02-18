package com.company.project.common.utils;

import com.company.project.common.config.Global;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

////////new import image crop--Rion

/**
 * @file ImageUtil.java
 * @author thon
 * @email thon.ju@gmail.com
 * @date Sep 11, 2013 3:47:43 PM
 * @description TODO
 */
public class ImageUtil {
    private static String IMG_URL = "";

    /**
     * 图片地址
     * @param url
     * @return
     */
    public static String formatUrl(String url) {
        IMG_URL =  Global.getConfig("project.img.url");
        if (url != null && !url.contains(IMG_URL)) {
            url = IMG_URL + url;
        }

        return url;
    }

    /**
     * 图片地址
     * @param id
     * @return
     */
    public static String formatUrl(Integer id) {
        IMG_URL =  Global.getConfig("project.img.url");
        String url="";
        if (id != null && url != null && !url.contains(IMG_URL)) {
            return url = IMG_URL + id;
        }else {
            return url = IMG_URL + 0;
        }

    }

	/**
	 * 图片水印
	 *
	 * @param pressImg
	 *            水印图片
	 * @param targetImg
	 *            目标图片
	 * @param x
	 *            修正值 默认在中间
	 * @param y
	 *            修正值 默认在中间
	 * @param alpha
	 *            透明度
	 */
	public final static void pressImage(String pressImg, String targetImg, int x, int y, float alpha) {
		try {
			File img = new File(targetImg);
			Image src = ImageIO.read(img);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			Image src_biao = ImageIO.read(new File(pressImg));
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao,
					null);
			// 水印文件结束
			g.dispose();
			ImageIO.write((BufferedImage) image, "jpg", img);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文字水印
	 *
	 * @param pressText
	 *            水印文字
	 * @param targetImg
	 *            目标图片
	 * @param fontName
	 *            字体名称
	 * @param fontStyle
	 *            字体样式
	 * @param color
	 *            字体颜色
	 * @param fontSize
	 *            字体大小
	 * @param x
	 *            修正值
	 * @param y
	 *            修正值
	 * @param alpha
	 *            透明度
	 */
	public static void pressText(String pressText, String targetImg, String fontName, int fontStyle, Color color,
			int fontSize, int x, int y, float alpha) {
		try {
			File img = new File(targetImg);
			Image src = ImageIO.read(img);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);
			g.dispose();
			ImageIO.write((BufferedImage) image, "jpg", img);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成空白图片
	 * @param str 图片上的提示文字
	 * @param file 文件路径
	 * @param font 字体属性
	 * @param height 高度
	 * @param width 宽度
	 */
	public static void newBlankImage(String str, File file, Font font, int height, int width){
        try {
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
			Graphics2D g = bi.createGraphics();
			g.setColor(new Color(245, 245, 245));
			g.fillRect(0, 0, width, height);
			g.setFont(font);
			g.setColor(Color.WHITE);

			FontMetrics fm = g.getFontMetrics(font);
			java.awt.geom.Rectangle2D rect = fm.getStringBounds(str, g);
			int textWidth = (int) (rect.getWidth());
			int textHeight = (int) (rect.getHeight());

			int x = (width - textWidth) / 2;
			int y = (height - textHeight) / 2 + fm.getAscent();

			g.drawString(str, x, y);
			g.dispose();
			ImageIO.write(bi, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public static int getLength(String text) {
		int length = 0;
		for (int i = 0; i < text.length(); i++) {
			if (new String(text.charAt(i) + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length / 2;
	}

	// -----------------压缩图片--------------------
	public static void resize(File originalFile, File thumnailFile, String dimension, String format) {
		if (StringUtils.isNotBlank(dimension) && StringUtils.contains(dimension, "x")) {
			int width = NumberUtils.createInteger(StringUtils.substringBefore(dimension, "x"));
			int height = NumberUtils.createInteger(StringUtils.substringAfter(dimension, "x"));
			resize(originalFile.getAbsolutePath(), thumnailFile.getAbsolutePath(), width, height, true, false, format);
		}
	}

	/**
	 * 压缩图片（默认为jpg格式）
	 *
	 * @param fromPath
	 *            源路径
	 * @param toPath
	 *            输出路径
	 * @param width
	 *            文件转换后的宽度
	 * @param height
	 *            文件转换后的高度
	 * @return
	 */
	public static boolean resize(String fromPath, String toPath, Integer width, Integer height) {
		return resize(fromPath, toPath, width, height, false, false, "jpg");
	}

	/**
	 * 压缩图片（默认为jpg格式）
	 *
	 * @param fromPath
	 *            源路径
	 * @param toPath
	 *            输出路径
	 * @param width
	 *            文件转换后的宽度
	 * @param height
	 *            文件转换后的高度
	 * @return
	 */
	public static boolean resize(String fromPath, String toPath, Integer width, Integer height, boolean isOtherThan) {
		return resize(fromPath, toPath, width, height, isOtherThan, true, "jpg");
	}

	/**
	 * 压缩图片
	 *
	 * @param fromPath
	 *            源路径
	 * @param toPath
	 *            输出路径
	 * @param width
	 *            文件转换后的宽度
	 * @param height
	 *            文件转换后的高度
	 * @param isOtherThan
	 *            是否等比缩小 true:等比 false:不等比
	 * @param isFiller
	 *            是否补白，前提条件是图片是等比缩小
	 * @param format
	 *            文件压缩后的格式(jpg格式文件小，gif质量差，文件稍微大一点，bmp文件大，清晰度还可以)
	 * @return
	 */
	public static boolean resize(String fromPath, String toPath, Integer width, Integer height, boolean isOtherThan,
			boolean isFiller, String format) {
		try {
			// 将图片文件读入到缓存中
			BufferedImage inImage = ImageIO.read(new FileInputStream(fromPath));
			int scaledW = width;
			int scaledH = height;
			if (isOtherThan) {
				int imw = inImage.getWidth();
				int imh = inImage.getHeight();

				double scale;
				if (imh <= height && imw <= width) {
					scale = 1;
				} else {
					scale = (double) width / (double) imw;
				}

				/*
				 * else if (imh > imw) scale = (double) height / (double) imh;
				 * else scale = (double) width / (double) imw;
				 */

				scaledW = (int) (scale * imw);
				scaledH = (int) (scale * imh);
			}

			// 将图片分辨率转成指定分辨率（高+宽）
			AdvancedResizeOp resampleOp = new ResampleOp(scaledW, scaledH);
			// 重新生成图片
			BufferedImage rescaledTomato = resampleOp.filter(inImage, null);

			// 是否补白(必须是等比缩放才会补白)
			if (isFiller && isOtherThan) {

				BufferedImage image = new BufferedImage(width, height, inImage.getType());
				Graphics2D g = image.createGraphics();

				// g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
				// 1.0f));
				// image =
				// g.getDeviceConfiguration().createCompatibleImage(width,
				// height, Transparency.TRANSLUCENT);
				// g.dispose();
				// g = image.createGraphics();

				g.setColor(new Color(255, 255, 255));
				// g.setStroke(new BasicStroke(1));

				g.fillRect(0, 0, width, height);
				if (width == rescaledTomato.getWidth(null))
					g.drawImage(rescaledTomato, 0, (height - rescaledTomato.getHeight(null)) / 2,
							rescaledTomato.getWidth(null), rescaledTomato.getHeight(null), Color.white, null);
				else
					g.drawImage(rescaledTomato, (width - rescaledTomato.getWidth(null)) / 2,
							(height - rescaledTomato.getHeight(null)) / 2, rescaledTomato.getWidth(null),
							rescaledTomato.getHeight(null), Color.white, null);

				// g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
				g.dispose();
				rescaledTomato = image;
			}

			// 将文件写入输入文件
			ImageIO.write(rescaledTomato, format, new File(toPath));

			// System.out.println("转后图片高度和宽度：" + rescaledTomato.getHeight() +
			// ":" + rescaledTomato.getWidth());

			return true;
		} catch (IOException e) {
			return false;
		}

	}

	/*
	 * @author Rion
	 *
	 * 图像切割（改） *
	 *
	 * @param srcImageFile 源图像地址
	 *
	 * @param dirImageFile 新图像地址
	 *
	 * @param x 目标切片起点x坐标
	 *
	 * @param y 目标切片起点y坐标
	 *
	 * @param destWidth 目标切片宽度
	 *
	 * @param destHeight 目标切片高度
	 *
	 * @param scale 缩略图宽度与真实图片宽度之比
	 */
	public static void abscut(String srcImageFile, String dirImageFile, int x, int y, int destWidth, int destHeight,
			double scale) {
		try {

			if (scale != 0) {
				x = (int) (x / scale);
				y = (int) (y / scale);
				destHeight = (int) (destHeight / scale);
				destWidth = (int) (destWidth / scale);
			}

			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度
			if (srcWidth >= destWidth && srcHeight >= destHeight) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				// 改进的想法:是否可用多线程加快切割速度
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
				img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, "JPEG", new File(dirImageFile));
			}
		} catch (Exception e) {
		}
	}

	public static byte[] readInputStream(String urlStr) throws Exception {
		URL url = new URL(urlStr);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		InputStream inStream = conn.getInputStream();

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	public static Boolean rotateImage(final BufferedImage bufferedImage, final int radian, final String dirImageFile) {
		Boolean resultBoolean = false;
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		BufferedImage dstImage = null;
		AffineTransform affineTransform = new AffineTransform();

		if (radian == 180) {
			affineTransform.translate(width, height);
			dstImage = new BufferedImage(width, height, bufferedImage.getType());
		} else if (radian == 90) {
			affineTransform.translate(height, 0);
			dstImage = new BufferedImage(height, width, bufferedImage.getType());
		} else if (radian == 270) {
			affineTransform.translate(0, width);
			dstImage = new BufferedImage(height, width, bufferedImage.getType());
		}

		affineTransform.rotate(Math.toRadians(radian));

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, rh);
		try {
			ImageIO.write(affineTransformOp.filter(bufferedImage, dstImage), "JPEG", new File(dirImageFile));
			resultBoolean = true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return resultBoolean;
	}

	public static BufferedImage convertToBufferedImage(File imgLoc) {
		BufferedImage img;
		try {
			img = ImageIO.read(imgLoc);
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			img = null;
		}

		return img;
	}

}

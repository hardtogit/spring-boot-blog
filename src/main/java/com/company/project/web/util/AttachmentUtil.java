package com.company.project.web.util;

import com.company.project.common.config.Global;
import com.company.project.common.utils.ImageUtil;
import com.company.project.model.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author THON
 * @email thon.ju@meet-future.com
 * @date 2011-11-24 上午10:58:18
 * @description:
 */
public final class AttachmentUtil {

	private static final String basePath = SystemUtils.getUserHome() + "/." +  Global.getConfig("project.name") + "/attachments";

	public static void read(Attachment attachment) {
		try {
		    // 默认图片
			File blankFile = new File(basePath, "not_exist.jpg");
			if (!blankFile.exists()) {
				Font font = new Font("Serif", Font.BOLD, 400);

				ImageUtil.newBlankImage("?", blankFile, font, 800, 800);
			}

			// 存在记录，则找到目标图片或缩略图
			File targetFile = getTargetFile(attachment);
			if(attachment.getId() != null && !StringUtils.equalsIgnoreCase(attachment.getSuffix(), ".gif") && StringUtils.isNotBlank(attachment.getDimension())) {
				File thumnailFile = getThumnailFile(attachment);
				if(!thumnailFile.exists() && targetFile.exists()) {
					ImageUtil.resize(targetFile, thumnailFile, attachment.getDimension(), attachment.getSuffix().substring(1));
				}

				blankFile = thumnailFile;
			}else if (targetFile.exists()) {
				blankFile = targetFile;
			}

			attachment.setBytes(FileUtils.readFileToByteArray(blankFile));
			} catch (IOException e) {
		}
	}

	public static void write(Attachment attachment){
		File targetFile = getTargetFile(attachment);
		try {
			FileUtils.writeByteArrayToFile(targetFile, attachment.getBytes());
		} catch (IOException e) {
		}
	}

	public static Attachment parse(MultipartFile file){
		Attachment attachment = new Attachment();

		attachment.setContentType(file.getContentType());
		attachment.setFileName(file.getOriginalFilename());
		attachment.setFileSize(((Long)file.getSize()).intValue());
		try {
			attachment.setBytes(file.getBytes());
		} catch (IOException e) {
		}
//		attachment.setCreateBy(userId);

		return attachment;
	}

	public static Attachment parse(byte[] fileData, String contentType, String fileName, Integer fileSize, Integer userId){
		Attachment attachment = new Attachment();
		attachment.setContentType(contentType);
		attachment.setFileName(fileName);
		attachment.setFileSize(fileSize);
		attachment.setBytes(fileData);
		attachment.setCreateBy(userId);

		return attachment;
	}

	public static void delete(Attachment attachment) {
		File targetFile = getTargetFile(attachment);
		if(StringUtils.isNotBlank(attachment.getDimension())) {
			File thumnailFile = getThumnailFile(attachment);
			FileUtils.deleteQuietly(thumnailFile);
		}
		FileUtils.deleteQuietly(targetFile);
	}

	// 获取缩略图
	private static File getThumnailFile(Attachment attachment) {
		StringBuffer pathBuffer = new StringBuffer(basePath);
		pathBuffer.append("/" + DateFormatUtils.format(attachment.getCreateTime(), "yyyy"));
		pathBuffer.append("/" + DateFormatUtils.format(attachment.getCreateTime(), "MMdd"));
		String name = attachment.getName() + "_" + attachment.getId() + "." +attachment.getDimension() + attachment.getSuffix();

		return new File(pathBuffer.toString(), name);
	}

	// 获取全图
	public static File getTargetFile(Attachment attachment) {
		StringBuffer pathBuffer = new StringBuffer(basePath);
		pathBuffer.append("/" + DateFormatUtils.format(attachment.getCreateTime(), "yyyy"));
		pathBuffer.append("/" + DateFormatUtils.format(attachment.getCreateTime(), "MMdd"));
		String name =  attachment.getName() + "_" + attachment.getId() + attachment.getSuffix();

		return new File(pathBuffer.toString(), name);
	}

	public static String getTargetFilePath(Attachment attachment) {
		StringBuffer pathBuffer = new StringBuffer(basePath);
		pathBuffer.append("/" + DateFormatUtils.format(attachment.getCreateTime(), "yyyy"));
		pathBuffer.append("/" + DateFormatUtils.format(attachment.getCreateTime(), "MMdd"));
		String name = pathBuffer+ "/"+attachment.getName() + "_" + attachment.getId() + attachment.getSuffix();
		return name;
	}

}

package com.shop.controllers.administratorSite.books;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shop.data.tables.Pictures;
import com.shop.others.RepositoriesAccess;

/**
 * Handles requests for the application file upload requests change for 1 file
 * which read every line
 */
@Controller
@RequestMapping("administratorSite/books")
public class FileUploadController {

	public static final String picturePath = "E:/WebShopPictures/";
	//private static final String testPicturePath = picturePath + "/linkPictures/";
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@RequestMapping("upload")
	public String hey() {
		return "upload";
	}

	@RequestMapping("uploadF")
	public String heey() {
		return "uploadMultiple";
	}

	@RequestMapping("uploadFil")
	public String heey(@RequestParam("name") String name) {
		System.out.println(name);
		return "upload";
	}

	@RequestMapping(value = "/uploadFilePicture", method = RequestMethod.POST)
	public String uploadFileHandlerPicture(@RequestParam(name = "name") String name,
			@RequestParam("file") MultipartFile file, Model model) {
		Pictures found = RepositoriesAccess.picturesRepository.findByName(name);

		if (found != null) {
			model.addAttribute("msg", "This picture already exist");
			return "administratorSite/booksCRUD/create";
		}

		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				String rootPath = System.getProperty("catalina.home");
				File dir = new File(picturePath);
				System.out.println("File : " + dir.getPath());
				if (!dir.exists())
					dir.mkdirs();

				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				System.out.println("Server : " + serverFile.getAbsolutePath());
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				Pictures picture = new Pictures();
				picture.setName(name);
				picture.setPath(dir.getPath());
				RepositoriesAccess.picturesRepository.save(picture);

				logger.info("Server File Location=" + serverFile.getAbsolutePath());

				model.addAttribute("msg", "Success");
				model.addAttribute("pictureName", name);
				model.addAttribute("msg", "Success");
				return "administratorSite/booksCRUD/create";
			} catch (Exception e) {
				model.addAttribute("msg", "Success");
				return "administratorSite/booksCRUD/create";
			}
		} else {
			model.addAttribute("msg", "error");
		}
		return "administratorSite/booksCRUD/create";
	}

	@RequestMapping(value = "/uploadFileLink")
	public String uploadFileHandlerLink(@RequestParam(name = "name") String name, @RequestParam("link") String link,
			Model model) throws IOException {
		Pictures found = RepositoriesAccess.picturesRepository.findByName(name);

		if (found != null) {
			model.addAttribute("msgLink", "This picture already exist");
			return "administratorSite/booksCRUD/create";
		}

		try {
			downloadImage(link, new File(picturePath).getAbsolutePath(), name);

		} catch (IOException ex) {
			model.addAttribute("msgLink", "error");
			return "administratorSite/booksCRUD/create";
		}

		Pictures picture = new Pictures(name, new File(picturePath).getAbsolutePath() + File.separator + name);
		RepositoriesAccess.picturesRepository.save(picture);

		model.addAttribute("pictureLinkName", name);
		model.addAttribute("msgLink", "Success");
		return "administratorSite/booksCRUD/create";
	}

	public static void downloadImage(String sourceUrl, String targetDirectory, String name)
			throws MalformedURLException, IOException, FileNotFoundException {
		URL imageUrl = new URL(sourceUrl);
		try (InputStream imageReader = new BufferedInputStream(imageUrl.openStream());
				OutputStream imageWriter = new BufferedOutputStream(
						new FileOutputStream(targetDirectory + File.separator + name));) {
			int readByte;
			// System.out.println(FilenameUtils.getName(sourceUrl));
			while ((readByte = imageReader.read()) != -1) {
				imageWriter.write(readByte);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
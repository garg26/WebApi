package com.neostencil.controllers;

import com.neostencil.requests.QuizSubmissionRequest;
import com.neostencil.requests.UnitRequest;
import com.neostencil.responses.UnitDetailResponse;
import com.neostencil.responses.UserQuizSubmissionResponse;
import com.neostencil.security.JwtTokenUtil;
import com.neostencil.services.LectureService;
import com.neostencil.utils.Utils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/v1")
public class LectureController {

	@Autowired
	LectureService lectureService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	private static final Logger LOGGER = LoggerFactory.getLogger(LectureController.class);

	@RequestMapping(value = "/player/watermark", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getPlayerWatermark() {
		try {

			String ip = Utils.getClientRealIP();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
			Date date = new Date();
			String dateString = dateFormat.format(date);
			String userEmail = jwtTokenUtil.getLoggedInUserName();

			int width = 160;
			int height = 30;

			// Constructs a BufferedImage of one of the predefined image types.
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			// Create a graphics which can be used to draw into the buffered
			// image
			Graphics2D g2d = bufferedImage.createGraphics();

			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, width, height);

			// create a string with yellow
			g2d.setColor(Color.BLACK);

			g2d.drawString(userEmail, 5, 16);
			g2d.drawString("IP: " + ip, 5, 28);
		//	g2d.drawString(dateString, 5, 40);

			// Disposes of this graphics context and releases any system
			// resources that it is using.
			g2d.dispose();

			// Create a byte array output stream.
			ByteArrayOutputStream bao = new ByteArrayOutputStream();

			// Write to output stream
			ImageIO.write(bufferedImage, "png", bao);

			return bao.toByteArray();
		} catch (IOException e) {
			LOGGER.error(e.getStackTrace().toString());
		}
		return null;
	}

	/**
	 * For playing a lecture
	 */
	@RequestMapping(value = "/admin_techops/lectures/{id}/play", method = RequestMethod.GET)
	@ResponseBody
	String playLecture(HttpServletRequest request, @PathVariable("id") int id) {

		String response = lectureService.playLectureAdmin(id);
		return response;
	}

	/**
	 * For playing a lecture
	 */
	@RequestMapping(value = "/free-lectures/{id}/play", method = RequestMethod.GET)
	@ResponseBody
	String playFreeLecture(HttpServletRequest request, @PathVariable("id") int id) {
		String response = lectureService.playLectureFree(id);
		return response;
	}

	@RequestMapping(value = "/admin_techops_student/units/{unitId}", method = RequestMethod.POST)
	@ResponseBody
	UnitDetailResponse getUnitDetails(HttpServletRequest request, @Valid @RequestBody UnitRequest unitRequest) {
		UnitDetailResponse response = lectureService.getUnitDetail(unitRequest,false);
		return response;
	}

	@RequestMapping(value = "/admin_techops_student/units/{unitId}/mobile", method = RequestMethod.POST)
	@ResponseBody
	UnitDetailResponse getUnitDetailsForMobile(HttpServletRequest request, @Valid @RequestBody UnitRequest unitRequest) {
		UnitDetailResponse response = lectureService.getUnitDetail(unitRequest,true);
		return response;
	}

	@RequestMapping(value = "/admin_techops/lectures/units/{unitId}", method = RequestMethod.GET)
	@ResponseBody
	UnitDetailResponse getUnitDetailsForAdmin(HttpServletRequest request, @PathVariable("unitId") int unitId) {
		UnitDetailResponse response = lectureService.getUnitDetailForAdmin(unitId);
		return response;
	}

	/**
	 * This will return either the unit play script or the pdf details of the
	 * assignment , depending upon the unit type
	 */
	@RequestMapping(value = "/user/units/{id}", method = RequestMethod.POST)
	@ResponseBody
	UnitDetailResponse unitDetail(HttpServletRequest request, @Valid @RequestBody UnitRequest unitRequest) {
		UnitDetailResponse response = lectureService.getUnitDetail(unitRequest, false);

		return response;

	}
	
	/**
	 * This is for getting the free unit details
	 * @param unitId
	 * @return
	 */
  @RequestMapping(value = "/units/{unitId}", method = RequestMethod.GET)
  @ResponseBody
  UnitDetailResponse getFreeUnitDetails(@PathVariable("unitId") int unitId) {
    UnitDetailResponse response = lectureService.getFreeUnitDetails(unitId);
    return response;
  }

	@RequestMapping(value="/admin_instructor/units/{unitId}",method=RequestMethod.GET)
	@ResponseBody
	UnitDetailResponse getTeacherUnitDetails(@PathVariable("unitId")int unitId)
	{
		UnitDetailResponse response=lectureService.getTeacherUnitDetails(unitId);
		return response;
	}

	@RequestMapping(value = "/user/unit/{unit_id}/quizsubmission/{quiz_id}",method = RequestMethod.PUT)
	@ResponseBody
	UserQuizSubmissionResponse calculateUserQuizStats(@PathVariable("unit_id") int unitId,@PathVariable("quiz_id") int quizId,@RequestParam("timetaken") String timeTaken, @RequestBody
			List<QuizSubmissionRequest> quizSubmissionRequest){

		long userID = jwtTokenUtil.getLoggedInUserID();
		return lectureService.calculateUserQuizStats(userID,unitId,quizId,timeTaken,quizSubmissionRequest);

	}
	
	@RequestMapping(value="/techops_admin/start-lecture/{unitId}",method=RequestMethod.PUT)
	public void startLecture(@PathVariable("unitId")int unitId)
	{
	  lectureService.markClassStarted(unitId);
	}
	

}

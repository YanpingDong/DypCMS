package com.tra.controller;

import com.tra.entity.PersonRole;
import com.tra.entity.PhotoEntity;
import com.tra.repository.IPersonRole;
import com.tra.repository.IPhoto;
import com.tra.web.model.StudentForm;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.List;

//https://blog.csdn.net/qq_34874784/article/details/83380144 分段上传
//https://blog.csdn.net/A1032453509/article/details/78045957 断点续传
@Api(value = "api to test connectivity with related services", produces = "application/json")
@Controller
@RequestMapping("/photo")
public class UploadController {
    @Autowired
    private IPhoto iPhoto;



    @ApiOperation(value = "get version info", notes = "get version info, type is String \n, example : vx.x.x-rcxx", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success, type is String \n, example : vx.x.x-rcxx ", response = String.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @PostMapping("/upload/actionf")
    @ResponseBody
    public String actionfile(StudentForm studentForm,
                         HttpServletRequest request) throws IOException {
        System.out.println("hahaha");

        InputStream is = studentForm.getStudentPhoto().getInputStream();
        byte[] studentPhotoData = new byte[(int) studentForm.getStudentPhoto().getSize()];
        int length = is.read(studentPhotoData);
        String fileName = studentForm.getStudentPhoto().getOriginalFilename();
        String destDirName = "/home/learlee/Downloads/TmpJar";
        File dir = new File(destDirName);
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File file = new File(destDirName+ fileName + ".txt");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(destDirName+ fileName + ".txt");
        fos.write(studentPhotoData,0,length);
        return "success";
    }
    //下载
    @ApiOperation(value = "get downPhotoById info", notes = "get version info, type is String \n, example : vx.x.x-rcxx", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success, type is String \n, example : vx.x.x-rcxx ", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/downPhotoByIdf")
    public void downPhotoByStudentIdf(
            @ApiParam(value = "The user/account to update subscriptions for", required = true, defaultValue = "dyptest")
            @RequestParam(value = "id") String id,
            final HttpServletResponse response) throws IOException {
        String destDirName = "/home/learlee/Downloads/test.jpg";
        File file = new File(destDirName);
        FileInputStream fileInputStream=new FileInputStream(file);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = fileInputStream.read(buff, 0, 100)) > 0) {
                         swapStream.write(buff, 0, rc);
                     }

        byte[] data = swapStream.toByteArray();
        String fileName = "test.jpg" ;
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.reset();
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("image/jpeg;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    @ApiOperation(value = "get version info", notes = "get version info, type is String \n, example : vx.x.x-rcxx", httpMethod = "POST")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success, type is String \n, example : vx.x.x-rcxx ", response = String.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @PostMapping("/upload/action")
    @ResponseBody
    public String action(StudentForm studentForm,
                         HttpServletRequest request) throws IOException {
        System.out.println("hahaha");

        InputStream is = studentForm.getStudentPhoto().getInputStream();
        byte[] studentPhotoData = new byte[(int) studentForm.getStudentPhoto().getSize()];
        is.read(studentPhotoData);
        String fileName = studentForm.getStudentPhoto().getOriginalFilename();
        PhotoEntity photoEntity = new PhotoEntity();
        //photoEntity.setPhotoData(studentPhotoData);
        photoEntity.setFileName(studentForm.getStudentName());
        photoEntity.setPhotoId("dyptest" );
        this.iPhoto.createPhoto(photoEntity);
        return "success";
    }


    @GetMapping("/upload")
    public String index(){
        return "upload"; //当浏览器输入/upload，会返回 /templates/upload.html页面
    }


    //下载
    @ApiOperation(value = "get downPhotoById info", notes = "get version info, type is String \n, example : vx.x.x-rcxx", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success, type is String \n, example : vx.x.x-rcxx ", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/downPhotoById")
    public void downPhotoByStudentId(
            @ApiParam(value = "The user/account to update subscriptions for", required = true, defaultValue = "dyptest")
            @RequestParam(value = "id") String id,
            final HttpServletResponse response) throws IOException {
        PhotoEntity entity = this.iPhoto.getPhotoEntityByPhotoId(id);
        byte[] data = entity.getPhotoData();
        String fileName = entity.getFileName()== null ? "照片.png" : entity.getFileName();
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }


    //直接在页面显示
    @ApiOperation(value = "get getPhotoById info", notes = "get version info, type is String \n, example : vx.x.x-rcxx", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success, type is String \n, example : vx.x.x-rcxx ", response = void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "getPhotoById")
    public void getPhotoById (
            @ApiParam(value = "The user/account to update subscriptions for", required = true, defaultValue = "dyptest")
            @RequestParam(value = "id")
                    String id,
            final HttpServletResponse response) throws IOException {
        PhotoEntity entity = this.iPhoto.getPhotoEntityByPhotoId(id);
        byte[] data = entity.getPhotoData();
        response.setContentType("image/png");
        response.setCharacterEncoding("UTF-8");
        OutputStream outputSream = response.getOutputStream();
        InputStream in = new ByteArrayInputStream(data);
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf, 0, 1024)) != -1) {
            outputSream.write(buf, 0, len);
        }
        outputSream.close();
    }
}

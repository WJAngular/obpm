package cn.myapps.mobile2.view;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.myapps.util.StringUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: format
 * Date: 13-10-23
 * Time: 上午11:30
 * To change this template use File | Settings | File Templates.
 */
public class MbImageService extends HttpServlet {
	private static final long serialVersionUID = 1473678561056887026L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/jpeg;charset=UTF-8");
        String url = request.getParameter("_url");
        
        //客户端要求宽高
        String width = request.getParameter("_width");
        String height = request.getParameter("_height");
        url = getServletContext().getRealPath(url);
        try{
            OutputStream output = response.getOutputStream();
            File file = new File(url);
            if (!file.exists()) {
                throw new FileNotFoundException("文件不存在");
            }
            BufferedImage image = ImageIO.read(file); //输入流读取图片文件
            int newWidth = 43, newHeight = 43; //定义新图片的宽和高
            if(width != null && !StringUtil.isBlank(width)){
            	try{
            		newWidth = Integer.valueOf(width);
            	}catch(Exception e){
            	}
            }
            
            if(height != null && !StringUtil.isBlank(height)){
            	try{
            		newHeight = Integer.valueOf(height);
            	}catch(Exception e){
            	}
            }
            
            int []array = conversion(image.getWidth(),image.getHeight(), newWidth, newHeight);
            
            //创建一个新的图片对象
            BufferedImage imageNew = new BufferedImage(array[0],
                   array[1], BufferedImage.TYPE_INT_RGB);
            //在新图片对象里绘制原有图片内容,压缩图片
            imageNew.getGraphics().drawImage(
                    image.getScaledInstance(array[0], array[1],
                            Image.SCALE_SMOOTH), 0, 0, null);
            //通过response输出到客户端
            ImageIO.write(imageNew, "JPEG", output);
//            output.flush();
//            output.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//等比例缩放宽高换算
	public int[] conversion(int imageWidth,int imageHeight,int newWidth,int newHeight){
		int[] array = new int[2];
		
		try{
			
			System.out.print((double)imageWidth/(double)imageHeight);
			//计算原图的宽高比
			double imageAspectRatio = (double)Math.round((double)imageWidth/(double)imageHeight*100)/100;
			
			//虚拟宽高 
			double virtualWidth,virtualHight;
			
			//假设以高为标准
			virtualWidth = imageAspectRatio * newHeight;
			
			//判断假设宽是否大于新图的宽 大于则假设不成立 小于则假设成立
			if(virtualWidth > newWidth){
				//假设不成立 可以确定是由宽为标准
				virtualHight = newWidth / imageAspectRatio;
				virtualWidth = newWidth;
			}else{
				//假设成立
				virtualHight = newHeight;
			}
			
			//忽略小数点
			array[0] = (int) virtualWidth;
			array[1] = (int) virtualHight;
		}catch(Exception e){
			array[0] = newWidth;
			array[1] = newHeight;
		}
		
		
		return array;
	}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doPost(request, response);
    }

}

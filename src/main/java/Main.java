import com.alibaba.fastjson.JSONReader;
import pojo.Story;
import util.ResultStory;

import javax.xml.transform.Result;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: lucaszyang
 * @Date: 2019/05/27/19:24
 * @Description:
 */
public class Main {

    private static HttpURLConnection connection = null;

    public static void main(String[] args) {

//        String s = getStories("http://127.0.0.1:8080/hello");

        String s = getStories("http://127.0.0.1:8080/stories.json");


        ArrayList<ResultStory> storyArrayList = readJson("stories.json");


        ArrayList<ResultStory> needList = needAdd_sub(storyArrayList);

//        Scanner scanner = new Scanner(System.in);
//        int n = needList.size();
//        for (int i = 0; i < n; i++) {
//            String dev = scanner.next();
//
//            String out = add_subStory("http://127.0.0.1:8080/add_sub", dev);
//            System.out.println(out);
//        }

        String out = add_subStory("http://127.0.0.1:8080/add_sub", "dev");
        System.out.println(out);

//        ArrayList<Story> storyArrayList = new ArrayList<>();
//        JSONReader reader = null;
//        try {
//            reader = new JSONReader(new FileReader("stories.json"));
//            reader.startArray();
//
//            while (reader.hasNext()) {
//                storyArrayList.add(reader.readObject(Story.class));
//            }
//            reader.endArray();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            reader.close();
//        }
//
//        System.out.println(s);

    }


    public static String getStories(String httpUrl) {

//        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        OutputStream os = null;
        BufferedWriter bw = null;


        try {
            // 创建远程url连接对象
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：10000毫秒
            connection.setConnectTimeout(10000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(50000);

            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                  /D:/coding/idea/weishi/target/classes/stories.json
//                String dataUrl = Main.class.getClassLoader().getResource("./stories.json").getPath();
//                dataUrl = dataUrl.substring(1, dataUrl.length());
                os = new FileOutputStream("stories.json");
                bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");

                }
                result = sbf.toString();
                bw.write(result);
                bw.flush();


            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

    public static String add_subStory(String httpUrl, String dev) {

        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            // Read from the connection. Default is true.
            connection.setDoInput(true);
            // 默认是 GET方式
            connection.setRequestMethod("POST");
            // Post 请求不能使用缓存
            connection.setUseCaches(false);
            //设置本次连接是否自动重定向
            connection.setInstanceFollowRedirects(true);
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            // 意思是正文是urlencoded编码过的form参数
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());

            // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
            String content = "developer=" + URLEncoder.encode(dev, "UTF-8");
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
            out.writeBytes(content);
            //流用完记得关
            out.flush();
            out.close();
            //获取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
//            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                sb.append(line);
                sb.append("\r\n");
            }

            reader.close();
            //该干的都干完了,记得把连接断了
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    public static ArrayList<ResultStory> readJson(String url) {
        ArrayList<ResultStory> storyArrayList = new ArrayList<>();
        JSONReader reader = null;
        try {
            reader = new JSONReader(new FileReader(url));
            reader.startArray();

            while (reader.hasNext()) {
                storyArrayList.add(reader.readObject(ResultStory.class));
            }
            reader.endArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        return storyArrayList;
    }


    public static ArrayList<ResultStory> needAdd_sub(ArrayList<ResultStory> list) {
        ArrayList<ResultStory> rs = new ArrayList<>();
        for (ResultStory s : list) {
            if ("abandoned".equals(s.getStory().getStatus())) {
                rs.add(s);
            }
        }

        return rs;
    }


}

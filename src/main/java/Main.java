import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import pojo.Story;
import util.ResultStory;

import javax.xml.transform.Result;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
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
    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();


    public static void main(String[] args) {

//        读取系统中的配置文件
        Properties params = new Properties();
        try {
            //读取外在的系统路径，user.dir 的值是当前项目的跟路径
            String filePath = System.getProperty("user.dir").replace("\\", "/") + "/params.properties";
//            params.load(new FileInputStream("params.properties"));
            System.out.println(filePath);
            params.load(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")));

        } catch (IOException e) {
            System.out.println("读取配置文件出错");
            System.exit(0);
//            e.printStackTrace();
        }
//      读取各个参数
        String workspace_id = params.getProperty("workspace_id");
        String parent_id = params.getProperty("parent_id");
        String username = params.getProperty("username");
        String password = params.getProperty("password");
        String department = params.getProperty("department");
        String owner = params.getProperty("owner");
        String storyName = params.getProperty("storyName");

//        System.out.println(storyName);
//        try {
//            workspace_id = new String(params.getProperty("workspace_id").getBytes("ISO-8859-1"),"UTF-8");
//            parent_id = new String(params.getProperty("parent_id").getBytes("ISO-8859-1"),"UTF-8");
//            username = new String(params.getProperty("username").getBytes("ISO-8859-1"),"UTF-8");
//            password = new String(params.getProperty("password").getBytes("ISO-8859-1"),"UTF-8");
//            department = new String(params.getProperty("department").getBytes("ISO-8859-1"),"UTF-8");
//            owner = new String(params.getProperty("owner").getBytes("ISO-8859-1"),"UTF-8");
//            storyName = new String(params.getProperty("storyName").getBytes("ISO-8859-1"),"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

//      初步验证参数是否合法
        verifyParams(workspace_id, parent_id, department, owner, username, password, storyName);
//        对多元参数进行拆分
        String[] departments = department.split(" ");
        String[] owners = owner.split(" ");
        String[] storyNames = storyName.split(" ");
//        String s = getStories_Get("http://api.tapd.oa.com/stories.json?workspace_id=20398312&id=1020398312854902159", "lucaszyang", "37F8FF43-B57C-BC01-B79F-67C7A475B001");

        String toStoryUrl = "http://api.tapd.oa.com/stories.json?workspace_id=" + workspace_id + "&id=" + parent_id;
//        获取父需求
        String parent_s = getStory_Get(toStoryUrl, username, password);

        //对get_stories获取需求的json数据的解析，返回的是json数组：[{{}}{{}}{{}}]
        JSONArray parents = JSON.parseArray(parent_s);
        JSONObject parent = parents.getJSONObject(0);

        JSONObject parentData = parent.getJSONObject("Story");

//        for (int i = 0; i < departments.length; i++) {
//            String add = add_subStory("http://api.tapd.oa.com/stories/add_sub_story.json", workspace_id, parent_id, username, password, departments[i], owners[i], storyNames[i]);
//            //对新建子需求返回的json进行解析，返回的是json对象类型：{{}}
//            JSONObject subStory = JSON.parseObject(add);
//
//            JSONObject subStoryData = subStory.getJSONObject("Story");
//            String id = (String) subStoryData.get("id");
//            String update = update_subStory("http://api.tapd.oa.com/stories/" + id + ".json", workspace_id, id, username, password);
//            System.out.println(add);
//        }

//        对符合要求的父需求进行拆分
        if ("auditing".equals((String) parentData.get("status"))) {
            for (int i = 0; i < departments.length; i++) {


                String add = add_subStory("http://api.tapd.oa.com/stories/add_sub_story.json", workspace_id, parent_id, username, password, departments[i], owners[i], storyNames[i]);


                //对新建子需求返回的json进行解析，返回的是json对象类型：{{}}，获取子需求的ID
                JSONObject subStory = JSON.parseObject(add);
                JSONObject subStoryData = subStory.getJSONObject("Story");
                String id = (String) subStoryData.get("id");


                //对子需求status字段进行更新（初始化中无法直接设置该字段）
                String update = update_subStory("http://api.tapd.oa.com/stories/" + id + ".json", workspace_id, id, username, password);
                System.out.println(add);
            }
        } else {
            System.out.println("该父需求未处于评审通过待排期，无法新建子需求");
            System.exit(0);
        }


    }

    private static String update_subStory(String url, String workspace_id, String id, String username, String password) {
        String content = "";
        try {
            String p1 = "workspace_id=" + URLEncoder.encode(workspace_id, "UTF-8");
            String p2 = "id=" + URLEncoder.encode(id, "UTF-8");

            String p3 = "current_user=" + URLEncoder.encode(username, "UTF-8");

            String p4 = "status=" + URLEncoder.encode("auditing", "UTF-8");
            content = p1 + "&" + p2 + "&" + p3 + "&" + p4;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return doPost(url, content, username, password);
    }

    private static String add_subStory(String url, String workspace_id, String parent_id, String username, String password, String department, String owner, String storyName) {
        String content = "";
        try {
            String p1 = "workspace_id=" + URLEncoder.encode(workspace_id, "UTF-8");
            String p2 = "parent_id=" + URLEncoder.encode(parent_id, "UTF-8");
            String p3 = "owner=" + URLEncoder.encode(owner, "UTF-8");
            String p4 = "name=" + URLEncoder.encode("[" + department + "]" + storyName + "需求", "UTF-8");
            String p5 = "description=" + URLEncoder.encode("", "UTF-8");
            String p6 = "creator=" + URLEncoder.encode(username, "UTF-8");
            content = p1 + "&" + p2 + "&" + p3 + "&" + p4 + "&" + p5 + "&" + p6;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return doPost(url, content, username, password);

    }

    private static void verifyParams(String workspace_id, String parent_id, String department, String owner, String username, String password, String storyName) {
        if (workspace_id == null || parent_id == null || department == null || owner == null || username == null || password == null || storyName == null) {
            System.out.println("参数不正确");
            System.exit(0);
        } else {
            if (!isNumber(workspace_id) || !isNumber(parent_id)) {
                System.out.println("项目或者父需求ID不是整数");
                System.exit(0);
            }


            if (department.split(" ").length != owner.split(" ").length || department.split(" ").length != storyName.split(" ").length
                    || storyName.split(" ").length != owner.split(" ").length) {
                System.out.println("部门团队、处理人和需求名称数量不匹配");
                System.exit(0);
            }

        }
    }


    public static boolean isNumber(String s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) - '0' < 0 || s.charAt(i) - '0' > 9) {
                return false;
            }
        }

        return true;
    }

    public static String getStory_Get(String httpUrl, String username, String password) {

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
            // 设置读取远程返回的数据时间：50000毫秒
            connection.setReadTimeout(50000);

//            String base64e1 = "bHVjYXN6eWFuZzozN0Y4RkY0My1CNTdDLUJDMDEtQjc5Ri02N0M3QTQ3NUIwMDE=";
            String base64e = encoder.encodeToString((username + ":" + password).getBytes());
//            System.out.println(base64e);
//            System.out.println(base64e1);
            connection.setRequestProperty("Authorization", "Basic " + base64e);
//            connection.setRequestProperty("workspace_id","20398312");
            connection.setRequestProperty("Host", "api.tapd.oa.com");

            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            int code = connection.getResponseCode();

            if (code == 200) {
                System.out.println(code + ":访问成功");
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


            } else {
                System.out.println(code + ":访问出错");
                System.exit(0);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("访问超时");
            System.exit(0);

        } catch (MalformedURLException e) {
            System.out.println("url不正确");
            System.exit(0);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (FileNotFoundException e) {
            System.out.println("未找到文件");
            System.exit(0);
        } catch (ProtocolException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
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

//    public static String add_subStory_Post(String httpUrl, String workspace_id, String parent_id, String username, String password, String department, String owner, String storyName) {
//
//        StringBuffer sb = new StringBuffer();
//        try {
//            URL url = new URL(httpUrl);
//            connection = (HttpURLConnection) url.openConnection();
//            // 设置是否向connection输出，因为这个是post请求，参数要放在
//            // http正文内，因此需要设为true
//            connection.setDoOutput(true);
//            // Read from the connection. Default is true.
//            connection.setDoInput(true);
//            // 默认是 GET方式
//            connection.setRequestMethod("POST");
//            // Post 请求不能使用缓存
//            connection.setUseCaches(false);
//            //设置本次连接是否自动重定向
//            connection.setInstanceFollowRedirects(true);
//            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
//            // 意思是正文是urlencoded编码过的form参数
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
//            // 要注意的是connection.getOutputStream会隐含的进行connect。
//            String base64e = encoder.encodeToString((username + ":" + password).getBytes());
////            System.out.println(base64e);
//
//            connection.setRequestProperty("Authorization", "Basic " + base64e);
//
//            connection.setRequestProperty("Host", "api.tapd.oa.com");
//            connection.connect();
//            DataOutputStream out = new DataOutputStream(connection
//                    .getOutputStream());
//
//            // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
//            String p1 = "workspace_id=" + URLEncoder.encode(workspace_id, "UTF-8");
//            String p2 = "parent_id=" + URLEncoder.encode(parent_id, "UTF-8");
//            String p3 = "owner=" + URLEncoder.encode(owner, "UTF-8");
//            String p4 = "name=" + URLEncoder.encode("[" + department + "]" + storyName + "需求", "UTF-8");
//            String p5 = "description=" + URLEncoder.encode("", "UTF-8");
//            String p6 = "creator=" + URLEncoder.encode(username, "UTF-8");
////            String p7 = "status=" + URLEncoder.encode("auditing", "UTF-8");
//            String content = p1 + "&" + p2 + "&" + p3 + "&" + p4 + "&" + p5 + "&" + p6;
//            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
//            out.writeBytes(content);
//
//            out.flush();
//            out.close();
//
//            int code = connection.getResponseCode();
//            if (code == 200) {
//                //获取响应
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String line;
////            StringBuffer sb = new StringBuffer();
//                while ((line = reader.readLine()) != null) {
////                System.out.println(line);
//                    sb.append(line);
//                    sb.append("\r\n");
//                }
//
//                reader.close();
//                //把连接断了
//                connection.disconnect();
//            } else {
//                System.out.println(code + ":访问出错");
//                System.exit(0);
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return sb.toString();
//    }


    public static String doPost(String httpUrl, String content, String username, String password) {

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
            String base64e = encoder.encodeToString((username + ":" + password).getBytes());
//            System.out.println(base64e);

            connection.setRequestProperty("Authorization", "Basic " + base64e);

            connection.setRequestProperty("Host", "api.tapd.oa.com");
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());

            // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
//            String p1 = "workspace_id=" + URLEncoder.encode(workspace_id, "UTF-8");
//            String p2 = "parent_id=" + URLEncoder.encode(parent_id, "UTF-8");
//            String p3 = "owner=" + URLEncoder.encode(owner, "UTF-8");
//            String p4 = "name=" + URLEncoder.encode("[" + department + "]" + storyName + "需求", "UTF-8");
//            String p5 = "description=" + URLEncoder.encode("", "UTF-8");
//            String p6 = "creator=" + URLEncoder.encode(username, "UTF-8");
//            String p7 = "status=" + URLEncoder.encode("auditing", "UTF-8");
//            String content = p1 + "&" + p2 + "&" + p3 + "&" + p4 + "&" + p5 + "&" + p6;
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
            out.writeBytes(content);

            out.flush();
            out.close();

            int code = connection.getResponseCode();
            if (code == 200) {
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
                //把连接断了
                connection.disconnect();
            } else {
                System.out.println(code + ":访问出错");
                System.exit(0);
            }

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

package in.xiandan.keystorebrowser.library;

import com.google.gson.Gson;

import java.io.File;

/**
 * author  dengyuhan
 * created 2019/5/14 17:57
 */
public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();
        try {
            File input = new File(args[0]);
            if (input.getAbsolutePath().endsWith(".apk")) {
                System.out.println(gson.toJson(BaseResponse.success(new ApkParser().parser(input))));
            } else {
                System.out.println(gson.toJson(BaseResponse.success(new KeyStoreParser().parser(input, args[1]))));
            }
        } catch (Exception e) {
            if (e.getMessage().contains("password was incorrect")){
                System.out.println(gson.toJson(BaseResponse.error("密码错误")));
            }else{
                System.out.println(gson.toJson(BaseResponse.error(e.getMessage())));
            }
        }
    }
}

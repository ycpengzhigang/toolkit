package com.touna.dubbo.consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSONObject;

public class Consumer {
    public static void main(String[] args) throws IOException {
        // 普通编码配置方式
        ApplicationConfig application = new ApplicationConfig();
        application.setName("risk-management");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://172.30.18.111:2181?backup=172.30.18.112:2181,172.30.19.109:2181");

        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface("com.touna.credit.riskmanagement.facade.intf.CreditRecordFacade");
        // reference.setGroup("stable");
        reference.setGeneric(true); // 声明为泛化接口

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(reference);

        Scanner scan = new Scanner(System.in);
        String read = scan.nextLine();
        System.out.println("请输入文件名：" + read);

        List<String> strList = readLineToList(read);
        
        for (String str : strList) {
            System.out.println("参数:" + str);
            JSONObject parseObject = JSONObject.parseObject(str);
            Object result = genericService.$invoke("creditSubmit",
                    new String[] { "com.touna.credit.riskmanagement.facade.dto.CreditParamDTO", "java.lang.String" },
                    new Object[] { parseObject, GenerateUUID() });
            System.out.println("result: " + result);

            // 睡眠两秒
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // ignore
            }

            System.out.println("休眠一次");
        }
    }

    public static List<String> readLineToList(String logFile) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        logFile = castToJavaFilePath(logFile);

        String command = "cat " + logFile;
        String[] cmdarray = { "/bin/sh", "-c", command };

        Process exec = runtime.exec(cmdarray);

        InputStreamReader fileReader = new InputStreamReader(exec.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> list = new ArrayList<String>();
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            if (str.trim().length() >= 1) {
                list.add(str);
            }
        }

        return list;
    }

    public static String GenerateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * 将文件路径转换为Java可识别的路径
     * 
     * @param path
     *            文件路径
     * @return
     */
    public static final String castToJavaFilePath(String path) {
        String FILE_SEPERATOR = "/";

        if (path == null)
            return null;

        // 反斜杠
        path = replace(path, "\\", FILE_SEPERATOR);
        path = replace(path, "\\\\", FILE_SEPERATOR);

        // 斜杠
        path = replace(path, "//", FILE_SEPERATOR);
        path = replace(path, "////", FILE_SEPERATOR);
        path = replace(path, "//////", FILE_SEPERATOR);
        path = replace(path, "////////", FILE_SEPERATOR);

        path = replace(path, "/", FILE_SEPERATOR);
        path = replace(path, "//", FILE_SEPERATOR);
        path = replace(path, "///", FILE_SEPERATOR);
        path = replace(path, "////", FILE_SEPERATOR);

        path = replace(path, "${FILE_SEPERATOR}", FILE_SEPERATOR);

        return path;
    }

    public static final String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static final String replace(final String text, final String searchString, final String replacement,
            int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static final boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
}

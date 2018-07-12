package com.ganzib.papa.sprider.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.selector.Html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;


/**
 * Created with IntelliJ IDEA.
 * Description: 网页转图片处理类，使用外部CMD 需安装 phantomjs-2.1.1-windows
 * User: GanZiB
 * Date: 2018-07-12
 * Time: 下午3:53
 * Email: ganzib4fun@gmail.com
 */

public class PhantomTools {
    // windows下phantomjs位置
    private static final String path = "/home/ganzib/Tools/phantomjs-2.1.1-linux-x86_64/";
    // 要执行的js，要使用决定路径
    private static final String jsPath = path + "bin/getPage.js ";

    /**
     * 根据传入的url，调用phantomjs进行下载，并返回base64流信息
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String getPageText(String url) throws IOException {
        Runtime rt = Runtime.getRuntime();

        final String cmd = path + "bin/phantomjs " + jsPath + url.trim();
        // 执行CMD命令
        Process process = rt.exec(cmd);

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuffer sbf = new StringBuffer();
        String tmp = "";
        while ((tmp = br.readLine()) != null) {
            sbf.append(tmp);
        }
        return sbf.toString();
    }

    public static void main(String[] args) {
        String url = "https://club.autohome.com.cn/bbs/thread/c41f3f18e0445987/74451211-1.html";
        try {
            String pageText = getPageText(url);
            Html html = new Html(pageText);
            System.out.println(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
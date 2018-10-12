package com.shou.crabscore.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.util.UsernameUtil;
import com.shou.crabscore.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shou.crabscore.controller.RunController.HeaderInfo.*;
import static java.security.MessageDigest.getInstance;

/**
 * 高校体育攻击接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "一键跑步接口")
public class RunController {

    /**
     * Header数据类
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Data
    class HeaderInfo {
        /**
         * 公共Header
         * 不知所云的字段BDA9F42E0C8A294ECDF5CC72AAE6A701
         */
        final static String ACCEPT_LANGUAGE = "Accept-Language";
        final static String ACCEPT_LANGUAGE_VALUE = "en-US,en;q=0.8";
        final static String USER_AGENT = "User-Agent";
        final static String USER_AGENT_VALUE = "okhttp-okgo/jeasonlzy";
        final static String VERSION_CODE = "versionCode";
        final static String VERSION_CODE_VALUE = "308";
        final static String VERSION_NAME = "versionName";
        final static String VERSION_NAME_VALUE = "2.2.5";
        final static String PLATFORM = "platform";
        final static String PLATFORM_VALUE = "android";
        final static String XXVERSIONXX = "xxversionxx";
        final static String XXVERSIONXX_VALUE = "20180601";
        final static String UUID = "uuid";
        final static String UUID_VALUE = "B4D724F9EB50E6ED3DF5B571CCD7D4A8";
        final static String UTOKEN = "utoken";
        String utoken = "";
        final static String SECRET = "BDA9F42E0C8A294ECDF5CC72AAE6A701";
        final static String SECRET_VALUE = "0,0,0,0,1";
        final static String HOST = "host";
        final static String HOST_VALUE = "gxhttp.chinacloudapp.cn";
        final static String CONNECTION = "connection";
        final static String CONNECTION_VALUE = "Keep-Alive";
        final static String ACCEPT_ENCODING = "Accept-Encoding";
        final static String ACCEPT_ENCODING_VALUE = "gzip";
        final static String COOKIE = "Cookie";
        String cookie = "";
        final static String CONTENT_TYPE = "Content-Type";
        final static String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
        final static String CONTENT_LENGTH = "Content-Length";
        final static String CONTENT_LENGTH_VALUE = "33442";
        /**
         * 请求URL
         */
        String loginUrl = "http://gxhttp.chinacloudapp.cn/api/reg/login?sign=SIGN&data=DATA";
        String runPageUrl = "http://gxhttp.chinacloudapp.cn/api/run/runPage?sign=SIGN&data=DATA";
        String saveRunUrl = "http://gxhttp.chinacloudapp.cn/api/run/saveRunV2";
        String runDetailUrl = "http://gxhttp.chinacloudapp.cn/api/center/runDetailV2?sign=SIGN&data=DATA";

        /**
         * login 请求reqeust参数
         */
        final static String INFO = "info";
        final static String INFO_VALUE = "B4D724F9EB50E6ED3DF5B571CCD7D4A8";
        final static String MOBILE = "mobile";
        final static String PASSWORD = "password";
        final static String TYPE_DEVICE = "type";
        final static String TYPE_DEVICE_VALUE = "AndroidSDKbuiltforx86";
        final static String SIGN = "sign";
        final static String DATA = "data";

        /**
         * 设置Header
         *
         * @param httpRequest 请求
         * @param headerInfo  Header信息对象
         */
        void setRequestHeader(HttpRequest httpRequest, HeaderInfo headerInfo, boolean needSetCookie, boolean needSetUToken,
                              boolean isSaveRun) {
            httpRequest.header(ACCEPT_LANGUAGE, ACCEPT_LANGUAGE_VALUE);
            httpRequest.header(USER_AGENT, USER_AGENT_VALUE);
            httpRequest.header(VERSION_CODE, VERSION_CODE_VALUE);
            httpRequest.header(VERSION_NAME, VERSION_NAME_VALUE);
            httpRequest.header(PLATFORM, PLATFORM_VALUE);
            httpRequest.header(XXVERSIONXX, XXVERSIONXX_VALUE);
            httpRequest.header(UUID, UUID_VALUE);
            httpRequest.header(UTOKEN, headerInfo.utoken);
            httpRequest.header(SECRET, SECRET_VALUE);
            httpRequest.header(HOST, HOST_VALUE);
            httpRequest.header(CONNECTION, CONNECTION_VALUE);
            httpRequest.header(ACCEPT_ENCODING, ACCEPT_ENCODING_VALUE);
            if (needSetCookie) {
                httpRequest.header(COOKIE, headerInfo.cookie);
            }
            if (needSetUToken) {
                httpRequest.header(UTOKEN, headerInfo.utoken);
            }
            if (isSaveRun) {
                httpRequest.header(CONTENT_TYPE, CONTENT_TYPE_VALUE);
                httpRequest.header(CONTENT_LENGTH, CONTENT_LENGTH_VALUE);
            }
        }

        final static String USERID = "userid";
        String userId = "";
        /**
         * runPage请求request参数
         */
        final static String INITLOCATION = "initLocation";
        final static String INITLOCATION_VALUE = "121.8969640000,30.8829570000";
        final static String TYPE_RUN = "type";
        final static String TYPE_RUN_VALUE = "1";

        final static String RUNPAGEID = "runPageId";
        String runPageId = "";
        final static String IBEACON = "ibeacon";

        final static String RUNID = "runid";
        String runId = "";
    }

    /**
     * JsonDataToBody
     */
    static class Json2Package {

        private final static String SALT = "lpKK*TJE8WaIg%93O0pfn0#xS0i3xE$z";

        static String getSign(String json) {
            try {
                return myMd5Encode(SALT + "data" + json);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
        }

        static String getData(String json) {
            try {
                return URLEncoder.encode(json, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "error";
            }
        }

        private static String myMd5Encode(String inputStr) throws Exception {
            MessageDigest md5 = getInstance("MD5");
            byte[] bytes = inputStr.getBytes(StandardCharsets.UTF_8);
            byte[] md5Bytes = md5.digest(bytes);
            StringBuilder hexValue = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                int value = ((int) md5Byte) & 0xff;
                if (value < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(value));
            }
            return hexValue.toString();
        }
    }

    private Map<String, String> getLoginBody(String mobile, String password) {
        Map<String, String> data = new HashMap<>(4);
        data.put(INFO, INFO_VALUE);
        data.put(MOBILE, mobile);
        data.put(PASSWORD, password);
        data.put(TYPE_DEVICE, TYPE_DEVICE_VALUE);
        Map<String, String> body = new HashMap<>(2);
        String dataJson = JSON.toJSONString(data);
        body.put(SIGN, Json2Package.getSign(dataJson));
        body.put(DATA, Json2Package.getData(dataJson));
        log.info("login body:" + body);
        return body;
    }

    private Map<String, String> getRunPageBody(String userId) {
        Map<String, String> data = new HashMap<>(4);
        data.put(INITLOCATION, INITLOCATION_VALUE);
        data.put(TYPE_RUN, TYPE_RUN_VALUE);
        data.put(USERID, userId);
        Map<String, String> body = new HashMap<>(2);
        String dataJson = JSON.toJSONString(data);
        body.put(SIGN, Json2Package.getSign(dataJson));
        body.put(DATA, Json2Package.getData(dataJson));
        log.info("run page body:" + body);
        return body;
    }

    private Map<String, Object> getSaveRunBody(HeaderInfo headerInfo, JSONObject mustPassNodeOne, String startTime,
                                               String endTime, Double distance, boolean isGirl) {
        JSONObject position = mustPassNodeOne.getJSONObject("position");
        position.put("speed", 0.0);
        mustPassNodeOne.put("position", position);
        JSONArray bNode = new JSONArray();
        bNode.add(mustPassNodeOne);
        Map<String, Object> data = new HashMap<>(16);
        data.put("buPin", "120");
        try {
            data.put("duration", String.valueOf((DateFormat.getDateTimeInstance().parse(startTime).getTime()) -
                    DateFormat.getDateTimeInstance().parse(endTime).getTime() / 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        data.put("endTime", endTime);
        data.put("frombp", "1");
        data.put("goal", (isGirl ? "1.50" : "2.00"));
        data.put("real", String.valueOf(distance));
        data.put("runPageId", headerInfo.runPageId);
        data.put("speed", "8'20''");
        data.put("startTime", startTime);
        data.put("type", "1");
        data.put("userid", headerInfo.userId);
        data.put("totalNum", "0");
        String dataJsonString = JSON.toJSONString(data);
        JSONObject dataJsonObject = JSON.parseObject(dataJsonString);
        JSONObject myRun = JSON.parseObject("{\"bNode\":[{\"id\":\"736\",\"major\":\"shhy1\",\"minor\":\"00006\",\"name\":\"shhy100006\",\"number\":\"shhy100006\",\"position\":{\"latitude\":\"30.8818340000\",\"longitude\":\"121.8991570000\",\"speed\":0.0},\"type\":\"1\",\"uuid\":\"FDA50693-A4E2-4FB1-AFCF-C6EB07647825\"}],\"buPin\":\"120\",\"duration\":\"7200\",\"endTime\":\"2018-10-13 8:00:33\",\"frombp\":\"1\",\"goal\":\"2.00\",\"real\":\"50000.0164\",\"runPageId\":\"6982391\",\"speed\":\"8'20''\",\"startTime\":\"2018-10-13 6:00:39\",\"tNode\":[{\"latitude\":30.8829940000,\"longitude\":121.9014370000,\"speed\":0.0},{\"latitude\":30.8795210000,\"longitude\":121.8929660000,\"speed\":0.0},{\"latitude\":30.8859890000,\"longitude\":121.8948540000,\"speed\":0.0}],\"totalNum\":\"0\",\"track\":[{\"latitude\":30.880231391059027,\"longitude\":121.89797824435765},{\"latitude\":30.880685492621527,\"longitude\":121.89799289279514},{\"latitude\":30.880755208333333,\"longitude\":121.89796549479166},{\"latitude\":30.88081353081597,\"longitude\":121.89802056206597},{\"latitude\":30.88086018880208,\"longitude\":121.89809814453125},{\"latitude\":30.88091986762153,\"longitude\":121.89817762586806},{\"latitude\":30.880984429253473,\"longitude\":121.89826253255208},{\"latitude\":30.881039496527777,\"longitude\":121.89834689670138},{\"latitude\":30.8811083984375,\"longitude\":121.8983970811632},{\"latitude\":30.881160210503474,\"longitude\":121.89846218532986},{\"latitude\":30.881216905381944,\"longitude\":121.89852566189236},{\"latitude\":30.88126654730903,\"longitude\":121.89859375},{\"latitude\":30.881303439670138,\"longitude\":121.89867268880208},{\"latitude\":30.881350368923613,\"longitude\":121.89875515407986},{\"latitude\":30.881417643229167,\"longitude\":121.8988121202257},{\"latitude\":30.881494683159723,\"longitude\":121.89885959201389},{\"latitude\":30.88154324001736,\"longitude\":121.89893744574653},{\"latitude\":30.88157470703125,\"longitude\":121.89899305555555},{\"latitude\":30.881634385850695,\"longitude\":121.89906358506944},{\"latitude\":30.88171902126736,\"longitude\":121.8991148546007},{\"latitude\":30.881790907118056,\"longitude\":121.89918212890625},{\"latitude\":30.881795518663193,\"longitude\":121.89924370659722},{\"latitude\":30.881788736979168,\"longitude\":121.89923122829862},{\"latitude\":30.881771918402777,\"longitude\":121.89921847873264},{\"latitude\":30.881758626302084,\"longitude\":121.89920952690973},{\"latitude\":30.881754014756943,\"longitude\":121.89922092013889},{\"latitude\":30.88175265842014,\"longitude\":121.89924045138889},{\"latitude\":30.881737738715277,\"longitude\":121.89924696180556},{\"latitude\":30.88169460720486,\"longitude\":121.8992800564236},{\"latitude\":30.88166748046875,\"longitude\":121.89931559244792},{\"latitude\":30.8816357421875,\"longitude\":121.89935872395833},{\"latitude\":30.881608072916666,\"longitude\":121.89941162109375},{\"latitude\":30.881578233506943,\"longitude\":121.899443359375},{\"latitude\":30.88157172309028,\"longitude\":121.89952690972223},{\"latitude\":30.881583658854165,\"longitude\":121.89962212456597},{\"latitude\":30.881630316840276,\"longitude\":121.89970404730903},{\"latitude\":30.881696234809027,\"longitude\":121.89977023654514},{\"latitude\":30.881745062934026,\"longitude\":121.89983452690973},{\"latitude\":30.881719835069443,\"longitude\":121.8999378797743},{\"latitude\":30.881656358506945,\"longitude\":121.90000217013889},{\"latitude\":30.881585286458332,\"longitude\":121.9000634765625},{\"latitude\":30.881519911024306,\"longitude\":121.9001123046875},{\"latitude\":30.88147677951389,\"longitude\":121.90014838324653},{\"latitude\":30.8814697265625,\"longitude\":121.90015543619792},{\"latitude\":30.881467013888887,\"longitude\":121.90013264973959},{\"latitude\":30.88146511501736,\"longitude\":121.90010687934027},{\"latitude\":30.881490342881943,\"longitude\":121.900087890625},{\"latitude\":30.881614312065974,\"longitude\":121.9003586154514},{\"latitude\":30.88155815972222,\"longitude\":121.90044949001737},{\"latitude\":30.881492513020834,\"longitude\":121.90049886067709},{\"latitude\":30.88143473307292,\"longitude\":121.90054578993056},{\"latitude\":30.881365288628473,\"longitude\":121.90055148654514},{\"latitude\":30.8812939453125,\"longitude\":121.90056233723958},{\"latitude\":30.88124538845486,\"longitude\":121.90057508680556},{\"latitude\":30.881289333767363,\"longitude\":121.90055826822916},{\"latitude\":30.881366102430555,\"longitude\":121.90055636935764},{\"latitude\":30.881358235677084,\"longitude\":121.90056966145833},{\"latitude\":30.88134792751736,\"longitude\":121.90058349609374},{\"latitude\":30.881356879340277,\"longitude\":121.90057834201389},{\"latitude\":30.881350911458334,\"longitude\":121.90057562934028},{\"latitude\":30.881345757378472,\"longitude\":121.90056613498264},{\"latitude\":30.88134819878472,\"longitude\":121.90058892144097},{\"latitude\":30.88137369791667,\"longitude\":121.90067220052083},{\"latitude\":30.881445041232638,\"longitude\":121.90075303819444},{\"latitude\":30.881506890190973,\"longitude\":121.90084391276042},{\"latitude\":30.881569010416666,\"longitude\":121.90092936197917},{\"latitude\":30.881604817708332,\"longitude\":121.90102430555555},{\"latitude\":30.88166748046875,\"longitude\":121.90111056857639},{\"latitude\":30.88169867621528,\"longitude\":121.90120659722223},{\"latitude\":30.881732584635415,\"longitude\":121.90129123263888},{\"latitude\":30.88178738064236,\"longitude\":121.90136501736112},{\"latitude\":30.88186740451389,\"longitude\":121.90141221788194},{\"latitude\":30.881820203993055,\"longitude\":121.90141059027778},{\"latitude\":30.88179470486111,\"longitude\":121.90136040581598},{\"latitude\":30.88186794704861,\"longitude\":121.90138617621528},{\"latitude\":30.881921929253473,\"longitude\":121.90145236545139},{\"latitude\":30.881989203559026,\"longitude\":121.90152262369791},{\"latitude\":30.882061360677085,\"longitude\":121.90158148871528},{\"latitude\":30.88207926432292,\"longitude\":121.90165825737847},{\"latitude\":30.88209255642361,\"longitude\":121.901689453125},{\"latitude\":30.882103407118056,\"longitude\":121.9017125108507},{\"latitude\":30.882104220920137,\"longitude\":121.90170627170139},{\"latitude\":30.882112901475693,\"longitude\":121.90173448350694},{\"latitude\":30.882096082899306,\"longitude\":121.90176188151041},{\"latitude\":30.882077907986112,\"longitude\":121.90175211588542},{\"latitude\":30.882063530815973,\"longitude\":121.90175401475695},{\"latitude\":30.882062717013888,\"longitude\":121.90174886067709},{\"latitude\":30.882069227430556,\"longitude\":121.90175862630208},{\"latitude\":30.88206787109375,\"longitude\":121.90175130208333},{\"latitude\":30.88204562717014,\"longitude\":121.90178195529514},{\"latitude\":30.88199734157986,\"longitude\":121.90179090711806},{\"latitude\":30.88195882161458,\"longitude\":121.90177354600695},{\"latitude\":30.88192626953125,\"longitude\":121.90176106770834},{\"latitude\":30.881909993489582,\"longitude\":121.90168565538194},{\"latitude\":30.88186740451389,\"longitude\":121.90163004557292},{\"latitude\":30.881867133246526,\"longitude\":121.90155598958333},{\"latitude\":30.881879611545138,\"longitude\":121.90148952907987},{\"latitude\":30.88187527126736,\"longitude\":121.90147677951389},{\"latitude\":30.88189208984375,\"longitude\":121.90151177300348},{\"latitude\":30.881927083333334,\"longitude\":121.90158013237847},{\"latitude\":30.881922743055554,\"longitude\":121.90166558159723},{\"latitude\":30.881873372395834,\"longitude\":121.90173719618056},{\"latitude\":30.881791178385416,\"longitude\":121.90177463107639},{\"latitude\":30.881697862413194,\"longitude\":121.9018031141493},{\"latitude\":30.881595323350695,\"longitude\":121.90183376736111},{\"latitude\":30.88149576822917,\"longitude\":121.90181179470486},{\"latitude\":30.881466742621527,\"longitude\":121.9017711046007},{\"latitude\":30.88143473307292,\"longitude\":121.90177137586805},{\"latitude\":30.881410047743056,\"longitude\":121.90177870008681},{\"latitude\":30.881407606336804,\"longitude\":121.90175726996527},{\"latitude\":30.881378851996526,\"longitude\":121.90173882378473},{\"latitude\":30.881355794270835,\"longitude\":121.9017068142361},{\"latitude\":30.88133246527778,\"longitude\":121.90167751736111},{\"latitude\":30.881341417100696,\"longitude\":121.901640625},{\"latitude\":30.8813818359375,\"longitude\":121.90166883680556},{\"latitude\":30.8813916015625,\"longitude\":121.90174723307291},{\"latitude\":30.881426866319444,\"longitude\":121.90179172092014},{\"latitude\":30.881471082899306,\"longitude\":121.90180284288195},{\"latitude\":30.881514756944444,\"longitude\":121.90181532118055},{\"latitude\":30.881563042534722,\"longitude\":121.9018199327257},{\"latitude\":30.88161078559028,\"longitude\":121.90181532118055},{\"latitude\":30.881651204427083,\"longitude\":121.90177734375},{\"latitude\":30.88169949001736,\"longitude\":121.90176025390625},{\"latitude\":30.881769205729167,\"longitude\":121.90176812065972},{\"latitude\":30.881846516927084,\"longitude\":121.90175130208333},{\"latitude\":30.88193060980903,\"longitude\":121.9017369249132},{\"latitude\":30.88200439453125,\"longitude\":121.90168972439236},{\"latitude\":30.882021213107638,\"longitude\":121.90163302951389},{\"latitude\":30.88204833984375,\"longitude\":121.90161295572916},{\"latitude\":30.882022026909723,\"longitude\":121.90157199435764},{\"latitude\":30.88198947482639,\"longitude\":121.90151557074653},{\"latitude\":30.88196478949653,\"longitude\":121.90143283420139},{\"latitude\":30.881940104166667,\"longitude\":121.90131727430555},{\"latitude\":30.881901041666666,\"longitude\":121.90121690538194},{\"latitude\":30.88186496310764,\"longitude\":121.9011195203993},{\"latitude\":30.881858995225695,\"longitude\":121.90102511935764},{\"latitude\":30.88187472873264,\"longitude\":121.90092447916666},{\"latitude\":30.881876356336807,\"longitude\":121.90083604600694},{\"latitude\":30.88183539496528,\"longitude\":121.9007470703125},{\"latitude\":30.88180392795139,\"longitude\":121.90065890842014},{\"latitude\":30.88177029079861,\"longitude\":121.90057237413194},{\"latitude\":30.881744520399305,\"longitude\":121.90047309027777},{\"latitude\":30.88171468098958,\"longitude\":121.90040907118056},{\"latitude\":30.881714952256946,\"longitude\":121.90041693793403},{\"latitude\":30.881723904079863,\"longitude\":121.90043240017361},{\"latitude\":30.881730685763888,\"longitude\":121.90045138888888},{\"latitude\":30.88172064887153,\"longitude\":121.90044460720486},{\"latitude\":30.88168755425347,\"longitude\":121.90045111762153},{\"latitude\":30.881653917100696,\"longitude\":121.90043592664931},{\"latitude\":30.881656901041666,\"longitude\":121.90038818359375},{\"latitude\":30.881670193142362,\"longitude\":121.90035400390624},{\"latitude\":30.881683756510416,\"longitude\":121.90032986111112},{\"latitude\":30.881678602430554,\"longitude\":121.90034125434028},{\"latitude\":30.881670735677083,\"longitude\":121.90035671657986},{\"latitude\":30.881666666666668,\"longitude\":121.9003648546007},{\"latitude\":30.8816748046875,\"longitude\":121.90036865234374},{\"latitude\":30.881715494791667,\"longitude\":121.90036187065972},{\"latitude\":30.881754014756943,\"longitude\":121.90036024305556},{\"latitude\":30.881792534722223,\"longitude\":121.90035400390624},{\"latitude\":30.881830512152778,\"longitude\":121.90034939236111},{\"latitude\":30.88180447048611,\"longitude\":121.90032253689236},{\"latitude\":30.881757269965277,\"longitude\":121.90032660590278},{\"latitude\":30.881712782118054,\"longitude\":121.90032904730903},{\"latitude\":30.881669379340277,\"longitude\":121.90032660590278},{\"latitude\":30.881636013454862,\"longitude\":121.90029541015625},{\"latitude\":30.88163058810764,\"longitude\":121.9002427842882},{\"latitude\":30.881627332899306,\"longitude\":121.90018934461806},{\"latitude\":30.881651204427083,\"longitude\":121.90016682942708},{\"latitude\":30.881678331163194,\"longitude\":121.90012315538195},{\"latitude\":30.88160617404514,\"longitude\":121.90024658203124},{\"latitude\":30.881620279947917,\"longitude\":121.90020263671875},{\"latitude\":30.88164496527778,\"longitude\":121.90015055338542},{\"latitude\":30.88165201822917,\"longitude\":121.9001500108507},{\"latitude\":30.881656629774305,\"longitude\":121.90014105902777},{\"latitude\":30.88166259765625,\"longitude\":121.90011935763889},{\"latitude\":30.881666395399307,\"longitude\":121.90011040581597},{\"latitude\":30.881670735677083,\"longitude\":121.90011610243056},{\"latitude\":30.8816650390625,\"longitude\":121.90009657118055},{\"latitude\":30.8816552734375,\"longitude\":121.90010064019097},{\"latitude\":30.88165228949653,\"longitude\":121.90008002387152},{\"latitude\":30.88164767795139,\"longitude\":121.90007541232639},{\"latitude\":30.881639539930557,\"longitude\":121.90006022135417},{\"latitude\":30.881634114583335,\"longitude\":121.90001573350695},{\"latitude\":30.88164035373264,\"longitude\":121.89995008680556},{\"latitude\":30.881641167534724,\"longitude\":121.89991373697917},{\"latitude\":30.88163275824653,\"longitude\":121.89987711588542},{\"latitude\":30.881627332899306,\"longitude\":121.89984890407986},{\"latitude\":30.881622178819445,\"longitude\":121.8998122829861},{\"latitude\":30.881612955729167,\"longitude\":121.89976806640625},{\"latitude\":30.8815966796875,\"longitude\":121.89970323350694},{\"latitude\":30.881580403645835,\"longitude\":121.89964246961806},{\"latitude\":30.881570909288193,\"longitude\":121.89958875868055},{\"latitude\":30.881571180555557,\"longitude\":121.89956298828125},{\"latitude\":30.88160590277778,\"longitude\":121.89953396267362},{\"latitude\":30.881673448350693,\"longitude\":121.8995738389757},{\"latitude\":30.881737738715277,\"longitude\":121.8996470811632},{\"latitude\":30.881792263454862,\"longitude\":121.89973225911459},{\"latitude\":30.88183837890625,\"longitude\":121.89982123480902},{\"latitude\":30.881885308159724,\"longitude\":121.89990017361112},{\"latitude\":30.881934136284723,\"longitude\":121.89996392144097},{\"latitude\":30.88199164496528,\"longitude\":121.90001898871527},{\"latitude\":30.88205322265625,\"longitude\":121.9000474717882},{\"latitude\":30.88212890625,\"longitude\":121.90007215711806},{\"latitude\":30.88220703125,\"longitude\":121.90012152777778},{\"latitude\":30.882269151475693,\"longitude\":121.90017876519097},{\"latitude\":30.882314181857637,\"longitude\":121.90025092230903},{\"latitude\":30.882373589409724,\"longitude\":121.90032253689236},{\"latitude\":30.882439778645832,\"longitude\":121.90039360894097},{\"latitude\":30.882508951822917,\"longitude\":121.90046061197917},{\"latitude\":30.88255425347222,\"longitude\":121.90054443359375},{\"latitude\":30.88261501736111,\"longitude\":121.9006298828125},{\"latitude\":30.882667100694444,\"longitude\":121.90070638020833},{\"latitude\":30.88271023220486,\"longitude\":121.90079372829861},{\"latitude\":30.88275661892361,\"longitude\":121.90088704427083},{\"latitude\":30.882797037760415,\"longitude\":121.90098551432291},{\"latitude\":30.882827962239585,\"longitude\":121.9010693359375},{\"latitude\":30.882842339409724,\"longitude\":121.90109537760416},{\"latitude\":30.88282769097222,\"longitude\":121.90108561197917},{\"latitude\":30.882821451822917,\"longitude\":121.90108561197917},{\"latitude\":30.882828776041666,\"longitude\":121.90114067925347},{\"latitude\":30.88282741970486,\"longitude\":121.90116997612847},{\"latitude\":30.882845052083333,\"longitude\":121.90119167751736},{\"latitude\":30.882880045572918,\"longitude\":121.90122938368056},{\"latitude\":30.882911512586805,\"longitude\":121.90126627604167},{\"latitude\":30.882938368055555,\"longitude\":121.90129909939236},{\"latitude\":30.882932400173612,\"longitude\":121.90126980251736},{\"latitude\":30.882884114583334,\"longitude\":121.90122748480903},{\"latitude\":30.88285942925347,\"longitude\":121.90122233072917},{\"latitude\":30.882848036024306,\"longitude\":121.90121120876736},{\"latitude\":30.882840169270832,\"longitude\":121.90117458767361},{\"latitude\":30.88282036675347,\"longitude\":121.90113959418403},{\"latitude\":30.88278076171875,\"longitude\":121.90114420572917},{\"latitude\":30.882753634982638,\"longitude\":121.9011474609375},{\"latitude\":30.882728678385416,\"longitude\":121.90117838541667},{\"latitude\":30.882720540364584,\"longitude\":121.90121988932292},{\"latitude\":30.882719184027778,\"longitude\":121.90122802734375},{\"latitude\":30.88272243923611,\"longitude\":121.9012158203125},{\"latitude\":30.88271728515625,\"longitude\":121.90120903862847},{\"latitude\":30.8827099609375,\"longitude\":121.90120903862847},{\"latitude\":30.882701009114584,\"longitude\":121.90120768229167},{\"latitude\":30.882703993055557,\"longitude\":121.90120225694444},{\"latitude\":30.88271240234375,\"longitude\":121.90120279947917},{\"latitude\":30.882722981770833,\"longitude\":121.90120144314236},{\"latitude\":30.88272976345486,\"longitude\":121.90120198567708},{\"latitude\":30.882734103732638,\"longitude\":121.90120361328125},{\"latitude\":30.88272243923611,\"longitude\":121.90116644965278},{\"latitude\":30.88278564453125,\"longitude\":121.90106553819444},{\"latitude\":30.88284396701389,\"longitude\":121.90101996527778},{\"latitude\":30.88290500217014,\"longitude\":121.9009605577257},{\"latitude\":30.882985026041666,\"longitude\":121.90090277777777},{\"latitude\":30.88309054904514,\"longitude\":121.90083197699653},{\"latitude\":30.884097764756945,\"longitude\":121.90045654296875},{\"latitude\":30.884301215277777,\"longitude\":121.90049994574653},{\"latitude\":30.884463975694445,\"longitude\":121.90049723307291},{\"latitude\":30.884660373263888,\"longitude\":121.90050238715278},{\"latitude\":30.884860026041668,\"longitude\":121.90049424913194},{\"latitude\":30.885100640190974,\"longitude\":121.90044976128472},{\"latitude\":30.885267469618057,\"longitude\":121.90037055121527},{\"latitude\":30.88546332465278,\"longitude\":121.900263671875},{\"latitude\":30.88568088107639,\"longitude\":121.90018907335069},{\"latitude\":30.885865342881946,\"longitude\":121.9000271267361},{\"latitude\":30.886037326388887,\"longitude\":121.8999197048611},{\"latitude\":30.88623019748264,\"longitude\":121.89978271484375},{\"latitude\":30.886347113715278,\"longitude\":121.89958821614583},{\"latitude\":30.886459147135415,\"longitude\":121.89940538194445},{\"latitude\":30.88659423828125,\"longitude\":121.89920491536458},{\"latitude\":30.88664306640625,\"longitude\":121.8990011935764},{\"latitude\":30.886650390625,\"longitude\":121.89889811197916},{\"latitude\":30.886669108072915,\"longitude\":121.89877766927083},{\"latitude\":30.88671902126736,\"longitude\":121.89861002604167},{\"latitude\":30.886771104600694,\"longitude\":121.89844672309027},{\"latitude\":30.886780870225696,\"longitude\":121.89837456597222},{\"latitude\":30.886776801215277,\"longitude\":121.89826443142361},{\"latitude\":30.886771375868054,\"longitude\":121.89809597439236},{\"latitude\":30.886730414496526,\"longitude\":121.89786729600695},{\"latitude\":30.886702473958334,\"longitude\":121.89759765625},{\"latitude\":30.886554090711805,\"longitude\":121.89668891059027},{\"latitude\":30.886453450520833,\"longitude\":121.89649685329861},{\"latitude\":30.886363661024305,\"longitude\":121.89627902560764},{\"latitude\":30.886284993489582,\"longitude\":121.89611870659722},{\"latitude\":30.886214192708334,\"longitude\":121.89593424479166},{\"latitude\":30.886122233072918,\"longitude\":121.89574028862847},{\"latitude\":30.88608832465278,\"longitude\":121.89555935329861},{\"latitude\":30.886055501302085,\"longitude\":121.89535481770834},{\"latitude\":30.88608859592014,\"longitude\":121.89518988715278},{\"latitude\":30.8860595703125,\"longitude\":121.89498101128473},{\"latitude\":30.886050075954863,\"longitude\":121.89481960720487},{\"latitude\":30.88598876953125,\"longitude\":121.8946875},{\"latitude\":30.88598388671875,\"longitude\":121.89461127387153},{\"latitude\":30.885909559461805,\"longitude\":121.89450792100695},{\"latitude\":30.885814073350694,\"longitude\":121.89434190538195},{\"latitude\":30.885017903645835,\"longitude\":121.89357449001736},{\"latitude\":30.88487575954861,\"longitude\":121.89358723958334},{\"latitude\":30.88466796875,\"longitude\":121.89356879340278},{\"latitude\":30.884471028645834,\"longitude\":121.89356770833334},{\"latitude\":30.884267035590277,\"longitude\":121.89357150607638},{\"latitude\":30.88408175998264,\"longitude\":121.89361870659722},{\"latitude\":30.883899468315974,\"longitude\":121.89365505642361},{\"latitude\":30.883726399739583,\"longitude\":121.89367160373264},{\"latitude\":30.883535698784723,\"longitude\":121.89369357638888},{\"latitude\":30.883311903211805,\"longitude\":121.8936732313368},{\"latitude\":30.883123372395833,\"longitude\":121.893623046875},{\"latitude\":30.882919108072915,\"longitude\":121.89350857204862},{\"latitude\":30.882716200086804,\"longitude\":121.89346055772569},{\"latitude\":30.88251925998264,\"longitude\":121.89341335720486},{\"latitude\":30.882288140190973,\"longitude\":121.89336859809028},{\"latitude\":30.88206814236111,\"longitude\":121.89338948567708},{\"latitude\":30.881844075520835,\"longitude\":121.89342475043402},{\"latitude\":30.881634385850695,\"longitude\":121.89353597005208},{\"latitude\":30.881464029947917,\"longitude\":121.89363932291667},{\"latitude\":30.88127414279514,\"longitude\":121.89368191189236},{\"latitude\":30.881132269965278,\"longitude\":121.89386962890624},{\"latitude\":30.881035970052082,\"longitude\":121.89407253689237},{\"latitude\":30.880867513020835,\"longitude\":121.89425618489584},{\"latitude\":30.88077935112847,\"longitude\":121.89445692274306},{\"latitude\":30.88069525824653,\"longitude\":121.89467746310764},{\"latitude\":30.880521375868057,\"longitude\":121.89478298611111},{\"latitude\":30.88033203125,\"longitude\":121.89477105034722},{\"latitude\":30.880174967447918,\"longitude\":121.89479383680556},{\"latitude\":30.880042046440973,\"longitude\":121.89488444010416},{\"latitude\":30.88001247829861,\"longitude\":121.89490505642361},{\"latitude\":30.879981282552084,\"longitude\":121.89500542534722},{\"latitude\":30.879954969618055,\"longitude\":121.89510660807292},{\"latitude\":30.879834798177082,\"longitude\":121.89605495876737},{\"latitude\":30.87983425564236,\"longitude\":121.89623345269098},{\"latitude\":30.87984076605903,\"longitude\":121.89639268663194},{\"latitude\":30.879845106336806,\"longitude\":121.8964523654514},{\"latitude\":30.879872775607637,\"longitude\":121.89716471354167},{\"latitude\":30.879888237847222,\"longitude\":121.89736382378472},{\"latitude\":30.879923231336807,\"longitude\":121.8976003689236},{\"latitude\":30.879987250434027,\"longitude\":121.89782199435764},{\"latitude\":30.880052897135418,\"longitude\":121.89798882378473},{\"latitude\":30.88007378472222,\"longitude\":121.89806830512153},{\"latitude\":30.88016357421875,\"longitude\":121.89807942708333},{\"latitude\":30.88026095920139,\"longitude\":121.89807861328126},{\"latitude\":30.880228407118057,\"longitude\":121.89802029079861},{\"latitude\":30.880126953125,\"longitude\":121.89806559244792},{\"latitude\":30.88014865451389,\"longitude\":121.89805257161458},{\"latitude\":30.880147840711807,\"longitude\":121.89806098090278},{\"latitude\":30.880103081597223,\"longitude\":121.89806477864583},{\"latitude\":30.88011284722222,\"longitude\":121.89806043836805},{\"latitude\":30.880118815104165,\"longitude\":121.89805772569444},{\"latitude\":30.88011284722222,\"longitude\":121.89805501302084},{\"latitude\":30.88011257595486,\"longitude\":121.89806640625},{\"latitude\":30.880125054253472,\"longitude\":121.89806206597223},{\"latitude\":30.880138075086805,\"longitude\":121.8980615234375},{\"latitude\":30.880142415364585,\"longitude\":121.89806396484374},{\"latitude\":30.880144856770833,\"longitude\":121.89804307725694},{\"latitude\":30.880125596788194,\"longitude\":121.89802327473959},{\"latitude\":30.880040147569446,\"longitude\":121.89801730685764},{\"latitude\":30.879971788194446,\"longitude\":121.8980211046007},{\"latitude\":30.879918619791667,\"longitude\":121.89804958767361},{\"latitude\":30.880482313368056,\"longitude\":121.89851888020833},{\"latitude\":30.88050835503472,\"longitude\":121.89856391059028},{\"latitude\":30.880541449652778,\"longitude\":121.89860297309028},{\"latitude\":30.880569932725695,\"longitude\":121.89866400824653},{\"latitude\":30.880530327690973,\"longitude\":121.89873860677083},{\"latitude\":30.880445421006943,\"longitude\":121.89877658420139},{\"latitude\":30.880387912326388,\"longitude\":121.89879041883681},{\"latitude\":30.88034152560764,\"longitude\":121.89881266276042},{\"latitude\":30.880335015190973,\"longitude\":121.89883355034722},{\"latitude\":30.880347222222223,\"longitude\":121.89885552300348},{\"latitude\":30.880343153211804,\"longitude\":121.89887749565972},{\"latitude\":30.880294596354165,\"longitude\":121.89889214409722},{\"latitude\":30.88026909722222,\"longitude\":121.89888102213541},{\"latitude\":30.880260416666665,\"longitude\":121.89887993706597},{\"latitude\":30.88029296875,\"longitude\":121.89885769314236},{\"latitude\":30.88029513888889,\"longitude\":121.89883870442708},{\"latitude\":30.880316569010418,\"longitude\":121.89883490668403},{\"latitude\":30.880384114583332,\"longitude\":121.89885172526041},{\"latitude\":30.880447862413195,\"longitude\":121.89883490668403},{\"latitude\":30.880492078993054,\"longitude\":121.89884168836805},{\"latitude\":30.88054009331597,\"longitude\":121.8988623046875},{\"latitude\":30.880582953559028,\"longitude\":121.89887234157986},{\"latitude\":30.880605740017362,\"longitude\":121.89886773003472}],\"trend\":[{\"x\":0.1,\"y\":49.966667},{\"x\":0.2,\"y\":49.916668},{\"x\":0.3,\"y\":49.966667},{\"x\":0.4,\"y\":50.016666},{\"x\":0.5,\"y\":50.05},{\"x\":0.6,\"y\":50.266666},{\"x\":0.7,\"y\":49.816666},{\"x\":0.8,\"y\":50.166668},{\"x\":0.9,\"y\":50.116665},{\"x\":1.0,\"y\":50.116665},{\"x\":1.1,\"y\":50.033333},{\"x\":1.2,\"y\":50.2},{\"x\":1.3,\"y\":50.0},{\"x\":1.4,\"y\":50.116665},{\"x\":1.5,\"y\":50.133335},{\"x\":1.6,\"y\":49.916668},{\"x\":1.7,\"y\":49.916668},{\"x\":1.8,\"y\":49.866665},{\"x\":1.9,\"y\":49.833332},{\"x\":2.0,\"y\":49.65},{\"x\":2.1,\"y\":50.033333},{\"x\":2.2,\"y\":49.9},{\"x\":2.3,\"y\":49.983334},{\"x\":2.4,\"y\":50.033333},{\"x\":2.5,\"y\":50.083332},{\"x\":2.6,\"y\":50.0},{\"x\":2.7,\"y\":50.033333},{\"x\":2.8,\"y\":50.616665},{\"x\":2.9,\"y\":50.15},{\"x\":3.0,\"y\":49.983334},{\"x\":3.1,\"y\":50.0},{\"x\":3.2,\"y\":49.85},{\"x\":3.3,\"y\":49.883335}],\"type\":\"1\",\"userid\":\"129835\"}");
        dataJsonObject.put("tNode", myRun.get("tNode"));
        dataJsonObject.put("track", myRun.get("track"));
        dataJsonObject.put("trend", myRun.get("trend"));
        dataJsonObject.put("bNode", bNode);
        String finalDataJson = JSON.toJSONString(dataJsonObject);
        Map<String, Object> body = new HashMap<>(2);
        body.put(SIGN, Json2Package.getSign(finalDataJson));
        body.put(DATA, finalDataJson);
        return body;
    }

    @PostMapping("/run")
    @ApiOperation(value = "高校体育一键跑步API")
    public Result<Object> run(@ApiParam(name = "mobile", value = "手机号", type = "String", defaultValue = "15000131965")
                              @RequestParam @NonNull String mobile,
                              @ApiParam(name = "password", value = "密码", type = "String", defaultValue = "cjh1998CJH")
                              @RequestParam @NonNull String password,
                              @ApiParam(name = "startTime", value = "开始时间", type = "String", defaultValue = "2018-10-13 6:30:33")
                              @RequestParam @NonNull String startTime,
                              @ApiParam(name = "endTime", value = "结束时间", type = "String", defaultValue = "2018-10-13 6:00:39")
                              @RequestParam @NonNull String endTime,
                              @ApiParam(name = "distance", value = "距离(meter)", type = "Double", defaultValue = "3000")
                              @RequestParam @NonNull Double distance,
                              @ApiParam(name = "isGirl", value = "是否为女生", type = "Boolean", defaultValue = "false")
                              @RequestParam @NonNull Boolean isGirl) {
        if (!UsernameUtil.mobile(mobile)) {
            return new ResultUtil<>().setErrorMsg(501, "手机号格式有误");
        } else {
            HeaderInfo headerInfo = new HeaderInfo();
            login(headerInfo, mobile, password);
            /*二 跑步界面请求*/
            Map<String, String> runPageBody = getRunPageBody(headerInfo.userId);
            headerInfo.runPageUrl = headerInfo.runPageUrl.replace("SIGN", runPageBody.get(SIGN)).
                    replace("DATA", runPageBody.get(DATA));
            log.info("runPageUrl:" + headerInfo.runPageUrl);
            HttpRequest runPageRequest = HttpRequest.get(headerInfo.runPageUrl);
            headerInfo.setRequestHeader(runPageRequest, headerInfo, true, true, false);
            String runPageJsonResult = runPageRequest.execute().body();
            log.info("run page result:" + runPageJsonResult);
            JSONObject runPageResponseResult = JSON.parseObject(runPageJsonResult);
            Integer code = runPageResponseResult.getInteger("code");
            if (!code.equals(CommonConstant.SUCCESS)) {
                return new ResultUtil<>().setErrorMsg("非学校规定运动时间段，您可以选择“自由跑“");
            }
            JSONObject runPageResponseData = runPageResponseResult.getJSONObject(DATA);
            headerInfo.setRunPageId(runPageResponseData.getString(RUNPAGEID));
            JSONArray mustPassNodeJsonArray = runPageResponseData.getJSONArray(IBEACON);
            JSONObject mustPassNodeOne = mustPassNodeJsonArray.getJSONObject(0);
            /*三 上传跑步数据请求*/
            Map<String, Object> saveRunBody = getSaveRunBody(headerInfo, mustPassNodeOne, startTime, endTime, distance, isGirl);
            String signAndData = JSON.toJSONString(saveRunBody);
            log.info("sign and data:" + signAndData);
            HttpRequest saveRunRequest = HttpRequest.post(headerInfo.saveRunUrl).form(saveRunBody);
            headerInfo.setRequestHeader(saveRunRequest, headerInfo, true, true, true);
            log.info("saveRunUrl:" + headerInfo.saveRunUrl);
            HttpResponse saveRunResult = saveRunRequest.execute();
            String saveRunJsonResult = saveRunResult.body();
            log.info("save run result:" + saveRunJsonResult);
            JSONObject saveRunResponseResult = JSON.parseObject(saveRunJsonResult);
            String finalCode = saveRunResponseResult.getString("code");
            String finalMessage = saveRunResponseResult.getString("msg");
            JSONObject saveRunResponseData = saveRunResponseResult.getJSONObject(DATA);
            String runId = saveRunResponseData.getString(RUNID);
            String description = saveRunResponseData.getString("dsc");
            return new ResultUtil<>().setData(description + " " + runId, finalMessage, Integer.parseInt(finalCode), true);
        }
    }

    private void login(HeaderInfo headerInfo, String mobile, String password) {
        /*一 登陆请求*/
        Map<String, String> loginBody = getLoginBody(mobile, password);
        headerInfo.loginUrl = headerInfo.loginUrl.replace("SIGN", loginBody.get(SIGN)).
                replace("DATA", loginBody.get(DATA));
        log.info("loginUrl:" + headerInfo.loginUrl);
        HttpRequest loginRequest = HttpRequest.get(headerInfo.loginUrl);
        headerInfo.setRequestHeader(loginRequest, headerInfo, false, false, false);
        HttpResponse loginResponse = loginRequest.execute();
        List<HttpCookie> cookie = loginResponse.getCookie();
        String loginJsonResult = loginResponse.body();
        JSONObject loginResponseResult = JSON.parseObject(loginJsonResult);
        JSONObject loginResponseData = loginResponseResult.getJSONObject(DATA);
        headerInfo.setUtoken(loginResponseData.getString(UTOKEN));
        headerInfo.setUserId(loginResponseData.getString(USERID));
        headerInfo.setCookie(String.valueOf(cookie.get(0)));
        log.info("login result:" + loginJsonResult);
    }
}

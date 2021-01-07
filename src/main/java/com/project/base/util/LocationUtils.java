package com.project.base.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author za-yinshaobo at 2021/1/7 16:36
 */
public class LocationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationUtils.class);

    private static final List<JsonNode> LOCATIONS = new ArrayList<>();

    static {
        try {
            String s = FileUtils.readTextContent(FileUtils.getResourceFilePath("files/location.json"));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(s);
            Iterator<JsonNode> elements = rootNode.elements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                LOCATIONS.add(next);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(calcLocation("120.03", "43.53"));
    }

    /**
     * 根据经纬度推算大致位置信息
     * @param longitude 经度
     * @param latitude 纬度
     * @return 位置json
     */
    public static JsonNode calcLocation(String longitude, String latitude) {
        LocationInfo point = new LocationInfo(longitude, latitude);
        double i = 10000;
        JsonNode jsonNode = null;
        for (JsonNode node : LOCATIONS) {
            //计算两点距离
            double distance = calcTwoPointsDistance(point, new LocationInfo(node.get("lon").textValue(), node.get("lat").textValue()));
            if (distance < i) {
                i = distance;
                jsonNode = node;
            }
        }
        return jsonNode;
    }

    /**
     * 计算两点距离
     * @param a 点a
     * @param b 点b
     * @return 距离
     */
    public static double calcTwoPointsDistance(LocationInfo a, LocationInfo b) {
        //经度差的平方
        BigDecimal powLon = a.getLongitude().subtract(b.getLongitude()).pow(2);
        //维度差的平方
        BigDecimal powLat = a.getLatitude().subtract(b.getLatitude()).pow(2);
        return Math.sqrt(Double.parseDouble(powLon.add(powLat).toString()));
    }

    private static class LocationInfo {
        private BigDecimal longitude;
        private BigDecimal latitude;
        public LocationInfo(String longitude, String latitude) {
            this.longitude = new BigDecimal(longitude);
            this.latitude = new BigDecimal(latitude);
        }

        public BigDecimal getLongitude() {
            return longitude;
        }

        public BigDecimal getLatitude() {
            return latitude;
        }
    }
}

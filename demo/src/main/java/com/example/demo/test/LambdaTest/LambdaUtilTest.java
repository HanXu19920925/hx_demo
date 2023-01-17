package com.example.demo.test.LambdaTest;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * List<T> Lambda表达式
 * @author hanxu
 */
@RestController
@RequestMapping("/lambdaUtil")
@Api(tags = "List<T> Lambda表达式")
public class LambdaUtilTest {

    @Data
    // 城市数据结构
    class City implements Serializable {

        private static final long serialVersionUID = -7716352032236707189L;

        public City() {}

        public City(String name, Integer size) {
            this.name = name;
            this.size = size;
        }

        /**
         * 城市
         */
        private String name;

        /**
         * 排名
         */
        private Integer size;
    }

    // 初始化数据
    public List<City> cities() {
        List<City> cityList = new ArrayList<>();
        {
            cityList.add(new City("上海",11));
            cityList.add(new City("武汉",22));
            cityList.add(new City("武汉",55));
            cityList.add(new City("上海",33));
            cityList.add(new City("北京",33));
            cityList.add(new City("深圳",43));
        }
        return cityList;
    }

    //去除集合重复值，只要含有重复元素则全部去掉
    public static <T> List<T> getNoDuplicateElements(List<T> list) {
        //获取元素出现频率，键为元素，值为元素出现的次数
        Map<T, Long> map = list.stream().collect(Collectors.groupingBy(p -> p,Collectors.counting()));
        System.out.println("获取元素出现频率，键为元素，值为元素出现的次数{}" + JSON.toJSONString(map));
        return map.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    //查询重复元素
    public static <T> List<T> getDuplicateElements(List<T> list) {
        return list.stream().collect(Collectors.collectingAndThen(Collectors
                .groupingBy(p -> p, Collectors.counting()), map->{
            map.values().removeIf(size -> size ==1);
            List<T> tempList = new ArrayList<>(map.keySet());
            return tempList;
        }));
    }

    //查询城市信息中名称重复的城市集合
    public static List<String> getDuplicateElementsForObject(List<City> list) {
        return list.stream().collect(Collectors.groupingBy(p -> p.getName(),Collectors.counting())).entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    //查询城市信息中名称不重复的城市集合
    public static List<String> getNoDuplicateElementsForObject(List<City> list){
        Map<String,List<City>> map = list.stream().collect(Collectors.groupingBy(City::getName));
        return map.entrySet().stream().filter(entry -> entry.getValue().size() == 1)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    //查询城市信息中名称重复的城市信息集合
    public static List<List<City>> getDuplicateObject(List<City> list) {
        return list.stream().collect(Collectors.groupingBy(City::getName)).entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());
    }

    //查询城市信息中名称不重复的城市信息集合
    public static List<City> getNoDuplicateObject(List<City> list) {
        List<City> cities = new ArrayList<>();
        list.stream().collect(Collectors.groupingBy(City::getName)).entrySet().stream()
                .filter(entry -> entry.getValue().size() ==1)
                .map(entry -> entry.getValue())
                .forEach(p -> cities.addAll(p));
        return cities;
    }

    //查询城市信息中根据名称去重复后的城市信息集合
    public static List<City> distinctObject(List<City> list) {
        return list.stream().filter(distinctByKey(City::getName)).collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    @GetMapping("/test")
    public void test() {
        //[{"name":"上海","size":11},{"name":"武汉","size":22},{"name":"武汉","size":55},{"name":"上海","size":33},{"name":"北京","size":33},{"name":"深圳","size":43}]
        List<City> cities = cities();
        System.out.println("查询所有的城市信息，List<City> {}" + JSON.toJSONString(cities));
        System.out.println("查询城市信息中名称重复的城市集合，List<String> {}" + getDuplicateElementsForObject(cities));
        System.out.println("查询城市信息中名称不重复的城市集合，List<String> {}" + getNoDuplicateElementsForObject(cities));
        System.out.println("查询城市信息中名称重复的城市信息集合，List<List<City>> {}" + getDuplicateObject(cities));
        System.out.println("查询城市信息中名称不重复的城市信息集合，List<City> {}" + getNoDuplicateObject(cities));
        System.out.println("查询城市信息中根据名称去重复后的城市信息集合，List<City> {}" + distinctObject(cities));

        Map<String, IntSummaryStatistics> intSummaryStatistics = cities.stream().
                collect(Collectors.groupingBy(i -> i.getName(), Collectors.summarizingInt(City::getSize)));
        System.out.println("查询各个城市信息中的数量，最大值，最小值，求和，平均值等信息，Map<String, IntSummaryStatistics> {}" + intSummaryStatistics);
        System.out.println("查询“武汉”城市信息中的排名累加{}" + intSummaryStatistics.get("武汉").getSum());

        //方式一
        String str = cities.stream().map(City::getName).collect(Collectors.joining(","));
        System.out.println("城市姓名以逗号拼接，String {}" + str);
        //方式二
        List<String> strList = cities.stream().map(City::getName).collect(Collectors.toList());
        System.out.println("城市姓名以逗号拼接，String {}" + String.join(",", strList));
        //方式三
        System.out.println("城市姓名以逗号拼接，String {}" + strList.stream().collect(Collectors.joining(",")));

        //List<String>和List<Integer>
        String user = "张三,李四";
        List<String> userList = Arrays.asList(user.split(",")).stream().map(string -> String.valueOf(string)).collect(Collectors.toList());
        System.out.println("List<String> {}" + JSON.toJSONString(userList));
        String info = "1,2,3";
        List<Integer> infoList = Arrays.stream(info.split(",")).map(String::trim).map(Integer::valueOf).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("List<Integer> {}" + JSON.toJSONString(infoList));

        //List转Map/Set转Map-1v1-以整个对象为value
        Map<String,City> mapResult = cities.stream().collect(Collectors.toMap(City::getName, city -> city, (k1, k2) -> k1));//用 (k1,k2)->k1 来设置，如果有重复的key,则保留key1,舍弃key2
        mapResult.forEach((k,v) -> System.out.println("list转map，以整个对象为value，Map<String,City> {}" + "，k=" + k + "，v=" + v));
        //List转Map/Set转Map-1v1-以对象某个属性为value
        Map<String,Integer> paramResult = cities.stream().collect(Collectors.toMap(City::getName, City::getSize, (k1, k2) -> k1));//用 (k1,k2)->k1 来设置，如果有重复的key,则保留key1,舍弃key2
        paramResult.forEach((k,v) -> System.out.println("list转map，以对象某个属性为value，Map<String,Integer> {}" + "，k=" + k + "，v=" + v));

        //List转Map/Set转Map-1vN-以整个对象为value
        Map<String,List<City>> mapRep = cities.stream().collect(Collectors.groupingBy(City::getName,Collectors.toList()));
        System.out.println("查询城市信息，并且根据城市进行分组，Map<String,List<City>> {}" + JSON.toJSONString(mapRep));

        //Map转List
        List<City> listResult = mapResult.entrySet().stream().map(key -> new City(key.getKey(),key.getValue().getSize())).collect(Collectors.toList());
        System.out.println("查询城市信息，map转list{}" + JSON.toJSONString(listResult));
    }

    public static void main(String[] args) {
        List<String> strList = Arrays.asList("1","2","2","3","3","4","4","5","6","7","8");
        System.out.println("去除集合重复值，只要含有重复元素则全部去掉{}" + getNoDuplicateElements(strList));
        System.out.println("查询重复元素{}" + getDuplicateElements(strList));
    }
}
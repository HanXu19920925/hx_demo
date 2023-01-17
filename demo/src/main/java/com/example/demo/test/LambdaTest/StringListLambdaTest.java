package com.example.demo.test.LambdaTest;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringListLambdaTest {

    //ForEach
    //集合遍历
    public static void testForEach(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
        }};
        list.forEach(s -> System.out.println(s));
    }

    //Collect
    //将操作后的对象转化为新的对象
    public static void testCollect(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("4");
        }};
        System.out.println(list.stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList()));
    }

    //Filter
    //只要满足Filter表达式的数据就可以留下来，不满足的数据被过滤掉
    public static void testFilter() {
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
        }};
        System.out.println(list.stream().filter(str -> StringUtils.equals(str, "1")).collect(Collectors.toList()));
    }

    //Map
    //map方法可以让我们进行一些流的转化，比如原来流中的元素是A，通过map操作，可以使返回的流中的元素是B，以及对数据进行计算
    public static void testMap() {
        List<String> list = new ArrayList<String>() {{
            add("A");
            add("B");
            add("C");
        }};
        //通过map方法将list中元素转化成小写
        System.out.println(list.stream().map(str -> str.toLowerCase()).collect(Collectors.toList()));

        System.out.println(list.stream().map(lang -> lang.toUpperCase()).collect(Collectors.joining("-")));

        List<Integer> salaryList = new ArrayList<Integer>();
        salaryList.add(1000);
        salaryList.add(1500);
        salaryList.add(1200);
        //通过map方法进行数据计算
        salaryList.stream().map((salary) -> salary + 0.2 * salary).forEach(System.out::println);

        System.out.println(list.stream().map(bean -> {
            if (bean.equals("A")){
                bean = "B";
            }
            return bean;
        }).collect(Collectors.toList()));
    }

    //MapToInt
    //mapToInt方法功能和map方法一样，只不过mapToInt返回的结果已经没有泛型，已经明确是int类型的流了
    public static void testMapToInt() {
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
        }};
        list.stream()
                .mapToInt(s -> Integer.valueOf(s))
                // 一定要有mapToObj，因为 mapToInt 返回的是IntStream，因为已经确定是int类型了，所有没有泛型的，而Collectors.toList()强制要求有泛型的流，所以需要使用mapToObj
                .mapToObj(s -> s)
                .collect(Collectors.toList());
        System.out.println(list.stream()
                .mapToDouble(s -> Double.valueOf(s))
                //DoubleStream/IntStream有许多sum（求和）、min（求最小值）、max（求最大值）、average（求平均值）等方法
                .sum());

        List<Integer> testList = Arrays.asList(1,2,3);
        IntSummaryStatistics stat = testList.stream().mapToInt((num) -> num).summaryStatistics();
        System.out.println("max:" + stat.getMax());
        System.out.println("min:" + stat.getMin());
        System.out.println("sum:" + stat.getSum());
        System.out.println("average:" + stat.getAverage());
    }

    //Distinct
    //distinct方法去重功能
    public static void testDistinct(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("2");
        }};
        System.out.println(list.stream().map(s -> Integer.valueOf(s)).distinct().collect(Collectors.toList()));
    }

    //Sorted
    //Sorted方法提供了排序的功能，并且允许我们自定义排序
    public static void testSorted(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
        }};
        System.out.println(list.stream().map(s -> Integer.valueOf(s))
                // 等同于.sorted(Comparator.naturalOrder())自然排序
                .sorted().collect(Collectors.toList()));

        //自定义排序器
        System.out.println(list.stream()
                .map(s -> Integer.valueOf(s))
                // 反自然排序
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
    }

    //groupingBy
    //groupingBy是能够根据字段进行分组，toMap是把List的数据格式转化成Map格式
    public static void testGroupBy(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("2");
        }};

        Map<String, List<String>> strList = list.stream().collect(Collectors.groupingBy(s -> {
            if("2".equals(s)) {
                return "2";
            }else {
                return "1";
            }
        }));

        System.out.println(JSON.toJSONString(strList));

        //统计数量
        List<String> stringList = new ArrayList<>();
        stringList.add("1001");
        stringList.add("1001");
        stringList.add("1002");
        stringList.add("1003");
        Map<String, Long>  map = stringList.stream().collect(Collectors
                .groupingBy(Function.identity(),Collectors.counting()));
        System.out.println(map);
    }

    //FindFirst
    //findFirst表示匹配到第一个满足条件的值就返回
    public static void testFindFirst(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
        }};

        list.stream()
                .filter(s->"2".equals(s))
                .findFirst()
                .get();

        // 防止空指针
        list.stream()
                .filter(s->"2".equals(s))
                .findFirst()
                // orElse表示如果findFirst返回null的话，就返回orElse里的内容
                .orElse("3");

        Optional<String> str = list.stream()
                .filter(s->"2".equals(s))
                .findFirst();

        // isPresent为true的话，表示value != null
        if(str.isPresent()){
            System.out.println(str.get());
            return;
        }
    }

    //Reduce
    //reduce方法允许我们在循环里面叠加计算值
    public static void testReduce(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
        }};

        list.stream()
                .map(s -> Integer.valueOf(s))
                // s1 和 s2 表示循环中的前后两个数
                .reduce((s1,s2) -> s1+s2)
                .orElse(0);

        System.out.println(list.stream()
                .map(s -> Integer.valueOf(s))
                // 第一个参数表示基数，会从 100 开始加
                .reduce(100,(s1,s2) -> s1+s2));
    }

    //Peek
    //peek方法很简单，我们在peek方法里面做任意没有返回值的事情，比如打印日志
    public static void testPeek(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
        }};

        System.out.println(list.stream().map(s -> Integer.valueOf(s))
                .peek(s -> System.out.println(s))
                .collect(Collectors.toList()));
    }

    //Limit
    //limit方法会限制输出值个数，入参是限制的个数大小
    public static void testLimit(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("3");
        }};

        System.out.println(list.stream()
                .map(s -> Integer.valueOf(s)).limit(2L).collect(Collectors.toList()));
    }

    //Max，Min
    //通过max、min方法，可以获取集合中最大、最小的对象
    public static void testMaxMin(){
        List<String> list = new ArrayList<String>() {{
            add("1");
            add("2");
            add("6");
        }};

        System.out.println(list.stream().max(Comparator.comparing(s -> Integer.valueOf(s))).get());
        System.out.println(list.stream().min(Comparator.comparing(s -> Integer.valueOf(s))).get());
    }

    public static void main(String[] args) {
        //testCollect();
        //testFindFirst();
        //testForEach();
        //testFilter();
        //testMap();
        //testMapToInt();
        //testDistinct();
        //testSorted();
        //testGroupBy();
        //testReduce();
        //testPeek();
        //testLimit();
        //testMaxMin();
    }
}

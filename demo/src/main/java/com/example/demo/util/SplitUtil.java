package com.example.demo.util;

import java.util.*;

/***
 * 平衡分组
 */
public class SplitUtil {

    /**
     * 平衡分组
     * @param sourceList 数据源
     * @param groupNum 被分配份数
     **/
    public static <T> List<T>[] spiltDataList(List<T> sourceList,int groupNum){
        List<T> [] group = new List[groupNum];

        for (int i = 0 ; i < groupNum ; i++) {
            group[i] = new ArrayList<>();
        }

        int sourceSize = sourceList.size();
        int batchNum = sourceSize % groupNum == 0 ? sourceSize / groupNum : sourceSize / groupNum + 1;

        for (int i = 1; i <= batchNum ; i++){
            if (i == batchNum){
                int finalBatchNum = sourceSize - (i - 1) * groupNum;
                for (int j = 0 ; j < finalBatchNum ; j++){
                    group[j].add(sourceList.get((i - 1) * groupNum + j));
                }
            }else {
                for (int j = 0 ; j < groupNum ; j++){
                    group[j].add(sourceList.get((i - 1) * groupNum + j));
                }
            }
        }
        return group;
    }
}
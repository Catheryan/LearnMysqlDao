package com.leayzh.main;

import java.util.ArrayList;
import java.util.Scanner;

import com.leayzh.bean.AnType;
import com.leayzh.bean.Animals;
import com.leayzh.util.BaseDAOx;

public class TestMain {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		BaseDAOx bd = new BaseDAOx();
		System.out.println("~~~欢迎来到动物管理系统~~~");
		while(true){
			System.out.println("1、查看动物  2、添加动物 3、查看类别 4、添加类别5、退出");
			int a = sc.nextInt();
			if(a==1){
				String sql = "select * from animals a,antype t where a.anid = t.anid ";
				Object[] obs ={};
				ArrayList<Animals> ar = bd.getListBySqlX(Animals.class, sql, obs);
				System.out.println("名称\t年龄\t类别");
				for(Animals an : ar){
					System.out.println(an.getName()+"\t"+an.getAge()+"\t"+an.getAnName());
				}
			}else if(a==2){
				ArrayList<AnType> ar = bd.getList(AnType.class);
				for(AnType an : ar){
					System.out.println("类别："+an.getAnName()+"类别号："+an.getAnId());
				}
				System.out.println("请输入动物名称、年龄 、类别号");
				Animals an = new Animals();
				an.setName(sc.next());
				an.setAge(sc.nextInt());
				an.setAnId(sc.nextInt());
				int id = bd.insertGetGeneratedKey(an);
				if(id!=0){
					System.out.println("插入成功！");
				}else{
					System.out.println("插入失败！");
				}
			}else if(a==3){
				ArrayList<AnType> ar = bd.getList(AnType.class);
				for(AnType an : ar){
					System.out.println("类别："+an.getAnName()+"类别号："+an.getAnId());
				}
			}else if(a==4){
				System.out.println("请输入要添加的类别名：");
				AnType an = new AnType();
				an.setAnName(sc.next());
				Boolean b = bd.insert(an);
				if(b){
					System.out.println("添加成功");
				}else{
					System.out.println("添加失败");
				}
			}else {
				break;
			}
		}
		
	}
}
package com.leayzh.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.leayzh.bean.Animals;
import com.leayzh.dao.AnimalsDao;

public class BaseDao {
	// 查询操作的dao方法
	public ArrayList getList(Class cl) {
		ArrayList arr = new ArrayList();
		Connection conn = BaseConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from " + cl.getSimpleName();
		System.out.println(sql);
		Field[] fd = cl.getDeclaredFields();

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Object ob = cl.newInstance();// ʵ�������
				for (Field ff : fd) {
					ff.setAccessible(true);
					ff.set(ob, rs.getObject(ff.getName()));
				}
				arr.add(ob);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseConnection.closeRes(conn, ps, rs);
		}
		return arr;
	}

	public Object getObById(Class cl, int id) {
		Object ob = null;
		Connection conn = BaseConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Field[] fi = cl.getDeclaredFields();
		String sql = "select * from " + cl.getSimpleName() + "where "
				+ fi[0].getName() + "=" + id;

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ob = cl.newInstance();
				for (Field ff : fi) {
					ff.setAccessible(true);
					ff.set(ob, rs.getObject(ff.getName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseConnection.closeRes(conn, ps, rs);
		}
		return ob;
	}

	// 通过类获取
	public ArrayList getListBySome(Class cl, String name, Object value) {
		ArrayList arr = new ArrayList();
		Connection conn = BaseConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from " + cl.getSimpleName() + " where  " + name
				+ " =" + "'" + value + "'";
		System.out.println(sql);
		Field[] fd = cl.getDeclaredFields();

		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Object ob = cl.newInstance();
				// 根据方法的名称进行循环
				// 将对应变量名填充到set方法里面
				for (Field ff : fd) {
					ff.setAccessible(true);
					ff.set(ob, rs.getObject(ff.getName()));
				}
				arr.add(ob);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseConnection.closeRes(conn, ps, rs);
		}
		return arr;
	}

	// 插入方法的dao方法

	public boolean insert(Object ob) {
		boolean b = false;
		Connection conn = BaseConnection.getConnection();
		PreparedStatement ps = null;
		Class cl = ob.getClass();
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(cl.getSimpleName());
		sb.append("(");
		Field[] fi = cl.getDeclaredFields();
		for (int i = 1; i < fi.length; i++) {
			sb.append(fi[i].getName());
			if (i != fi.length - 1) {
				sb.append(",");
			}
		}
		sb.append(") values (");
		for (int i = 1; i < fi.length; i++) {
			sb.append("?");
			if (i != fi.length - 1) {
				sb.append(",");
			}
		}
		sb.append(")");

		try {
			ps = conn.prepareStatement(sb.toString());
			for (int i = 1; i < fi.length; i++) {
				// ps根据的对象是sql语句中对象位置来进行辨别
				// fi[].get()获取的是对应的get方法的名字（获取的是对象object）,并且以private命名的顺序来的
				ps.setObject(i, fi[i].get(ob));
			}
			int a = ps.executeUpdate();
			if (a > 0) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseConnection.closeRes(conn, ps);
		}

		return b;
	}

	// 更新方法的dao方法
	public boolean update(Object ob) {
		boolean b = false;
		Connection conn = BaseConnection.getConnection();
		PreparedStatement ps = null;
		Class cl = ob.getClass();
		// "update animals set name=?,age=?,anid=? where id=?"
		StringBuffer sb = new StringBuffer();
		sb.append("update ");
		sb.append("animals ");
		sb.append("set ");
		Field[] fi = cl.getDeclaredFields();
		for (int i = 1; i < fi.length; i++) {
			sb.append(fi[i].getName());
			sb.append("=? ");
			if (i != fi.length - 1) {
				sb.append(",");
			}
		}
		sb.append(" where ");
		sb.append(fi[0].getName());
		sb.append("=?");
		try {
			// ps根据的对象是sql语句中对象位置来进行辨别
			// fi[].get()获取的是对应的get方法的名字（获取的是对象object）,并且以private命名的顺序来的
			ps = conn.prepareStatement(sb.toString());
			for (int i = 1; i < fi.length; i++) {
				fi[i].setAccessible(true);
				ps.setObject(i, fi[i].get(ob));
			}
			fi[fi.length].setAccessible(true);
			// 主键id是在最后面的一个
			ps.setObject(fi.length, fi[0].get(ob));
			int returnValue = ps.executeUpdate();
			if (returnValue > 0) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseConnection.closeRes(conn, ps);
		}
		return b;
	}

	public boolean delete(Class cl, String name, Object value) {
		boolean b = false;
		Connection conn = BaseConnection.getConnection();
		PreparedStatement ps = null;
		String sql = "delete from " + cl.getSimpleName() + " where " + name
				+ " = ?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setObject(1, value);
			int a = ps.executeUpdate();
			if (a > 0) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseConnection.closeRes(conn, ps);
		}
		return b;
	}

	public static void main(String[] args) {
		BaseDao bd = new BaseDao();
		// 查询方法的实例化
		/*
		 * ArrayList<Animals> ar=bd.getList(Animals.class); for (Animals an:ar)
		 * {
		 * System.out.println("id:"+an.getId()+" 名字:"+an.getName()+" 年龄:"+an.getAge
		 * ()); }
		 * 
		 * ArrayList<Animals> arr=bd.getListBySome(Animals.class, "name", "СȪ");
		 * for (Animals an:arr) {
		 * System.out.println("id:"+an.getId()+" 名字:"+an.getName
		 * ()+" 年龄:"+an.getAge()); }
		 */

		// 插入方法的实例化
		/*
		 * Animals an=new Animals(); an.setAge(20); an.setName("zhyry222");
		 * an.setId(8); AnimalsDao AnimalsDao=new AnimalsDao(); boolean
		 * b=AnimalsDao.update(an); System.out.println(b);
		 */

		// 删除的实例化操作
		/*
		 * boolean b=bd.delete(Animals.class, "name", "222");
		 * System.out.println(b);
		 */

		bd.getArrayList();
		ArrayList array=bd.getUrl();
		System.out.println(array);
	}

	ArrayList<Ti> arr = new ArrayList<Ti>();

	private void getArrayList() {
		arr.add(new Ti(1, "王二"));
		arr.add(new Ti(2, "张三"));
	}

	private ArrayList getUrl() {
		String fdname = null;
		ArrayList urlList = new ArrayList();
		Method metd = null;
		for (Object object : arr) {
			Class clazz = object.getClass();// 获取集合中的对象类型
			Field[] fds = clazz.getDeclaredFields();// 获取他的字段数组
			//for (Field field : fds) {// 遍历该数组
				//fdname = field.getName();// 得到字段名
				try {
					// 根据字段名找到对应的get方法，null表示无参数
					metd = clazz.getMethod("get" + "Name", null);
					// 调用该字段的get方法
					String name = (String) metd.invoke(object, null);
					urlList.add(name);
					System.out.println(name);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		//}
		return urlList;
	}

	/**
	 * @param src
	 *            源字符串
	 * @return 字符串，将src的第一个字母转换为大写，src为空时返回null
	 */
	public static String change(String src) {
		if (src != null) {
			StringBuffer sb = new StringBuffer(src);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return sb.toString();
		} else {
			return null;
		}
	}

}

class Ti {
	int age;
	String name;

	public Ti(int age, String name) {
		this.age = age;
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
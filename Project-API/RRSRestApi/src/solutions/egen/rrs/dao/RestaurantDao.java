/**
 * 
 */
package solutions.egen.rrs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import solutions.egen.rrs.model.Restaurant;
import solutions.egen.rrs.utils.DBUtil;

/**
 * @author Kesava
 *
 */
public class RestaurantDao
{

	/**
	 * Return all the restaurants in the database
	 * @return
	 */
	public List<Restaurant> getAllRestaurants()
	{
		List<Restaurant> result = null;
		Connection con = DBUtil.getConnection();
		Restaurant restaurant = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			result = new ArrayList<Restaurant>();
			ps = con.prepareStatement("Select * from restaurant_details");
			rs = ps.executeQuery();
			while(rs.next())
			{
				restaurant = new Restaurant();
				restaurant.setName(rs.getString("name"));
				restaurant.setOpen_time(rs.getTimestamp("open_time"));
				restaurant.setClose_time(rs.getTimestamp("close_time"));
				restaurant.setAddress1(rs.getString("address1"));
				restaurant.setAddress2(rs.getString("address2"));
				restaurant.setCity(rs.getString("city"));
				restaurant.setState(rs.getString("state"));
				restaurant.setZip(rs.getInt("zip"));
				restaurant.setEmail(rs.getString("email"));
				restaurant.setPhone(rs.getString("phone"));
				restaurant.setTable_1(rs.getInt("table_1"));
				restaurant.setTable_2(rs.getInt("table_2"));
				restaurant.setTable_4(rs.getInt("table_4"));
				restaurant.setTable_6(rs.getInt("table_6"));
				restaurant.setTable_8(rs.getInt("table_8"));
				restaurant.setId(rs.getInt("id"));
				result.add(restaurant);
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DBUtil.releaseResources(con,ps,rs);
		}
		return result;
	}

	/**
	 * Find restaurant in the database based on phone number
	 * @param phone
	 * @return
	 */
	public Restaurant getRestaurant(int id)
	{
		Restaurant result = null;
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			result = new Restaurant();
			ps = con.prepareStatement("SELECT * FROM restaurant_details WHERE "
					+ "id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next())
			{
				result.setName(rs.getString("name"));
				result.setOpen_time(rs.getTimestamp("open_time"));
				result.setClose_time(rs.getTimestamp("close_time"));
				result.setAddress1(rs.getString("address1"));
				result.setAddress2(rs.getString("address2"));
				result.setCity(rs.getString("city"));
				result.setState(rs.getString("state"));
				result.setZip(rs.getInt("zip"));
				result.setEmail(rs.getString("email"));
				result.setPhone(rs.getString("phone"));
				result.setTable_1(rs.getInt("table_1"));
				result.setTable_2(rs.getInt("table_2"));
				result.setTable_4(rs.getInt("table_4"));
				result.setTable_6(rs.getInt("table_6"));
				result.setTable_8(rs.getInt("table_8"));
				result.setId(rs.getInt("id"));
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DBUtil.releaseResources(con,ps,rs);
		}
		return result;
	}

	/**
	 * Create a new restaurant in the database
	 * @param restaurant
	 * @return
	 */
	public Restaurant createRestaurant(Restaurant restaurant)
	{
		Restaurant result = addRestaurant(restaurant);
		
		//Now since a new restaurant is added, add tables too
		TableDao tableDao = new TableDao();
		tableDao.addTables(result);
		
		return result;
	}

	/**
	 * @param restaurant
	 * @return
	 */
	private Restaurant addRestaurant(Restaurant restaurant)
	{
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = con.prepareStatement("INSERT INTO restaurant_details ("
					+ "name,open_time,close_time,address1,address2,city,"
					+ "state,zip,email,phone,table_1,table_2,table_4,table_6,"
					+ "table_8,auto_assign) VALUES "
					+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
					, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, restaurant.getName());
			ps.setTimestamp(2, restaurant.getOpen_time());
			ps.setTimestamp(3, restaurant.getClose_time());
			ps.setString(4, restaurant.getAddress1());
			ps.setString(5, restaurant.getAddress2());
			ps.setString(6, restaurant.getCity());
			ps.setString(7, restaurant.getState());
			ps.setInt(8, restaurant.getZip());
			ps.setString(9, restaurant.getEmail());
			ps.setString(10, restaurant.getPhone());
			ps.setInt(11, restaurant.getTable_1());
			ps.setInt(12, restaurant.getTable_2());
			ps.setInt(13, restaurant.getTable_4());
			ps.setInt(14, restaurant.getTable_6());
			ps.setInt(15, restaurant.getTable_8());
			ps.setInt(16, restaurant.getAUTO_ASSIGN());
			ps.execute();
			rs = ps.getGeneratedKeys();
			if(rs.next())
			{
				restaurant.setId(rs.getInt("id"));
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			DBUtil.releaseResources(con,ps,rs);
		}
		return restaurant;
	}

	/**
	 * Edit restaurant details based on the id
	 * @param restaurant
	 * @return
	 */
	public Restaurant editRestaurant(Restaurant restaurant)
	{
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//First create a reservation with default status and table ids
			//Then update based on the table availability.
			ps = con.prepareStatement("UPDATE restaurant_details "
					+ "SET name = ?,"
					+ "open_time = ?,"
					+ "close_time = ?,"
					+ "address1 = ?,"
					+ "address2 = ?,"
					+ "city = ?,"
					+ "state = ?,"
					+ "zip = ?,"
					+ "email = ?,"
					+ "phone = ?,"
					+ "table_1 = ?,"
					+ "table_2 = ?,"
					+ "table_4 = ?,"
					+ "table_6 = ?,"
					+ "table_8 = ?,"
					+ "auto_assign = ? "
					+ "WHERE id = ?",
					PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, restaurant.getName());
			ps.setTimestamp(2, restaurant.getOpen_time());
			ps.setTimestamp(3, restaurant.getClose_time());
			ps.setString(4, restaurant.getAddress1());
			ps.setString(5, restaurant.getAddress2());
			ps.setString(6, restaurant.getCity());
			ps.setString(7, restaurant.getState());
			ps.setInt(8, restaurant.getZip());
			ps.setString(9, restaurant.getEmail());
			ps.setString(10, restaurant.getPhone());
			ps.setInt(11, restaurant.getTable_1());
			ps.setInt(12, restaurant.getTable_2());
			ps.setInt(13, restaurant.getTable_4());
			ps.setInt(14, restaurant.getTable_6());
			ps.setInt(15, restaurant.getTable_8());
			ps.setInt(16, restaurant.getAUTO_ASSIGN());
			ps.execute();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			DBUtil.releaseResources(con,ps,rs);
		}
		
		checkAndUpdateTables(restaurant);
		return restaurant;
	}

	/**
	 * Delete a restaurant in database based on its id
	 * @param id
	 */
	public void deleteRestaurant(int id)
	{
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//First check if this customer is already present in database
			ps = con.prepareStatement("DELETE FROM restaurant_details WHERE "
					+ "id = ?");
			ps.setInt(1, id);
			ps.execute();
		}
		catch (SQLException e)
		{
			//TODO need custom exception
			e.printStackTrace();
		}
		finally
		{
			DBUtil.releaseResources(con,ps,rs);
		}
	}
	
	/**
	 * If the number of tables are changed in restaurant
	 * add or delete the tables as needed.
	 * @param restaurant
	 */
	private void checkAndUpdateTables(Restaurant restaurant)
	{
		//This is not requested so not implementing it
//		int table_1 = restaurant.getTable_1();
//		int table_2 = restaurant.getTable_1();
//		int table_4 = restaurant.getTable_1();
//		int table_6 = restaurant.getTable_1();
//		int table_8 = restaurant.getTable_1();
	}

}

package com.src.spatiotemporal.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Main.SpatioTemporal;

public class Input<hour> extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String function = request.getParameter("fun").toString();
		if(function.equals("originList"))
		{
			List<Block> blockList = SpatioTemporal.blockList;
			int sizeOfList = blockList.size();
			StringBuilder result = new StringBuilder();
			for(int index = 0; index < sizeOfList; index++)
			{
				Block b = blockList.get(index);
				result.append("<option value='"+b.getBlockId()+"'>"+b.getBlockName()+"</option>");

			}
			PrintWriter out = response.getWriter();
			out.println(result.toString());
		}
		else if(function.equals("hourList"))
		{
			StringBuilder result = new StringBuilder();
			for(int index = 0; index < 24; index++)
			{
				result.append("<option value='"+index+"'>"+index+"</option>");
			}
			PrintWriter out = response.getWriter();
			out.println(result.toString());
		}
		else if(function.equals("minList"))
		{
			StringBuilder result = new StringBuilder();
			for(int index = 0; index < 60; index++)
			{
				result.append("<option value='"+index+"'>"+index+"</option>");
			}
			PrintWriter out = response.getWriter();
			out.println(result.toString());
		}
		else if(function.equals("secList"))
		{
			StringBuilder result = new StringBuilder();
			for(int index = 0; index < 60; index++)
			{
				result.append("<option value='"+index+"'>"+index+"</option>");
			}
			PrintWriter out = response.getWriter();
			out.println(result.toString());
		}
		else if(function.equals("submit"))
		{
			
		}
	}
}

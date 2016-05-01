package com.src.spatiotemporal.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.src.spatiotemporal.Algorithms.Probabilistic;
import com.src.spatiotemporal.Algorithms.Algorithms;
import com.src.spatiotemporal.Algorithms.Baseline;
import com.src.spatiotemporal.Algorithms.Deterministic;
import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Node;

public class AlgorithmSelection extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			String algoSelection = request.getParameter("algo");
			String sec = request.getParameter("sec");
			String min = request.getParameter("min");
			String hour = request.getParameter("hour");
			String dd = request.getParameter("date");
			String mm = request.getParameter("mon");
			String yy = request.getParameter("year");
			
			String originBlockId = request.getParameter("origin");
			
			Block block = new Block();
			block.setBlockId(Long.parseLong(originBlockId));
			
			Algorithms chooseAlgo;
			if(algoSelection.equals("B"))
				chooseAlgo = new Baseline();
			else if(algoSelection.equals("D"))
				chooseAlgo = new Deterministic();
			else 
				chooseAlgo = new Probabilistic();
			
			Date inputDate = new Date();
			inputDate.setSeconds(Integer.parseInt(sec));
			inputDate.setMinutes(Integer.parseInt(min));
			inputDate.setHours(Integer.parseInt(hour));
			inputDate.setDate(Integer.parseInt(dd));
			inputDate.setMonth(Integer.parseInt(mm)-1);
			inputDate.setYear(Integer.parseInt(yy)-1900);

			List<Node> nodeList = chooseAlgo.fetchRoute(Long.parseLong(originBlockId), inputDate);			
			
			String json = new Gson().toJson(nodeList);
			PrintWriter out = response.getWriter();
			out.println(json);
	}

}

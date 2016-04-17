package com.src.spatiotemporal.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.src.spatiotemporal.Algorithms.Algorithms;
import com.src.spatiotemporal.Algorithms.Baseline;
import com.src.spatiotemporal.Algorithms.Deterministic;
import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Node;
import com.src.spatiotemporal.Main.SpatioTemporal;

public class AlgorithmSelection extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			String algoSelection = request.getParameter("algo");
			String sec = request.getParameter("sec");
			String min = request.getParameter("min");
			String hour = request.getParameter("hour");
			String originBlockId = request.getParameter("origin");
			
			Block block = new Block();
			block.setBlockId(Long.parseLong(originBlockId));
			
			Algorithms chooseAlgo;
			if(algoSelection.equals("B"))
				chooseAlgo = new Baseline();
			else if(algoSelection.equals("D"))
				chooseAlgo = new Deterministic();
			else 
				chooseAlgo = new Deterministic();
			
			List<Node> nodeList = chooseAlgo.fetchRoute(block,hour, min, sec);
			
			/* CODE FOR TESTING NEED TO REMOVE IT LATER*/			
			nodeList = new ArrayList<Node>();
			for(int i=0; i<5;i++)
				nodeList.add(SpatioTemporal.nodeList.get(i));
			/* CODE FOR TESTING NEED TO REMOVE IT LATER*/
			
			String json = new Gson().toJson(nodeList);
			PrintWriter out = response.getWriter();
			out.println(json);
	}

}

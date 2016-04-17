package com.src.spatiotemporal.Algorithms;

import java.util.List;

import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Node;

public class Deterministic implements Algorithms{

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	/*F(v, b) =  nb / distance(v, b)2
			nb = number of slots available
			v = current position of the vehicle
			b = position of block*/

	@Override
	public List<Node> fetchRoute(Block b,String hour, String min, String sec) {
		return null;
		
		
		/*Block b = Utils.findNeighbourhood(latitude, longitude);
		Block b1 = new Block(612312,"Polk St (3100-3198)",
				37.8054164152,-122.4236028968,37.8061265339,-122.423746731,7001,7002,2,8);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.S");
		Date d = null;
		try {
			d = format.parse("2012-04-06 0:19:35.0");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}*/
		
						
	}
	void calculateForce()
	{
		
	}

	@Override
	public void fetchRoute(Double latitude, Double longitude) {
		// TODO Auto-generated method stub
		
	}

}

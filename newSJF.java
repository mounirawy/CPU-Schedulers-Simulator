	import java.util.ArrayList;
	import java.util.Collections;
	
public class newSJF {


		ArrayList<String> processesname; 
		ArrayList<Integer> ArrivalTime; 
		ArrayList<Integer> BurstTime;
		ArrayList<Integer> priorities; 
		ArrayList<String> Order = new ArrayList<String>();
		ArrayList<Integer> WaitingTime;
		ArrayList<Integer> turnaroundtime;
		double averagewaitingtime;
		double averageturnaroundtime;
		int processesnumber;
		int contextswitch;
		
		newSJF(int contextswitch,ArrayList<Integer> ArrivalTime,ArrayList<Integer> BurstTime,ArrayList<String> processesname,ArrayList<Integer> priorities){
			this.processesname = processesname; 
			this.ArrivalTime = ArrivalTime;
			this.BurstTime = BurstTime;
			this.priorities = priorities;
			processesnumber = ArrivalTime.size();
			WaitingTime = new ArrayList<Integer>(Collections.nCopies(processesnumber, 0));
			turnaroundtime = new ArrayList<Integer>(Collections.nCopies(processesnumber, 0));
			this.contextswitch = contextswitch;
		}

		public void DoSJF() {

			int time = ArrivalTime.get(0);
			//int activeprocessindex = 0;
			int activeprocessindex = getfirst();
			String activeprocess = processesname.get(0); 
			int nextarrivaltime = ArrivalTime.get(1);
			boolean avail = true;
			int responsetime = ArrivalTime.get(0);
			int nextprocessidx = 1;
			int remainingtime;
			ArrayList<Integer> remainingtimelist = new ArrayList<Integer>(BurstTime);
			ArrayList<Integer> waitingqueueindex = new ArrayList<Integer>(); 
			ArrayList<Integer> waitingqueueremainingtime = new ArrayList<Integer>(); 
			ArrayList<Integer> waitingqueuepriority = new ArrayList<Integer>(); 
			while(true) {
				
				//if all process has finished their burst time
				if(checkzeros(remainingtimelist)) {
					break;
				}
				// if process arrived
	    		for(int i=0;i<processesnumber;i++) {
	    			if(activeprocessindex == i) {
	    				continue;
	    			}
	    			if(ArrivalTime.get(i) == time) {
	    				waitingqueueindex.add(i);
	    				waitingqueueremainingtime.add(BurstTime.get(i));
	    				waitingqueuepriority.add(priorities.get(i));
	    			}
	    		}
	    		///////		
	    		
	    		//if process has finshed 
	    		if(activeprocessindex != -1) {
		    	if(remainingtimelist.get(activeprocessindex) == 0) {
		    		if(!waitingqueueindex.isEmpty()) {
		    			
		    			//get one from waiting queue
		    			int idxhpro = getminidx(waitingqueuepriority);
		    			int minidx = waitingqueueindex.get(idxhpro);
		    			activeprocessindex = minidx;
		    			
		    			//add context switch time 
		    			if(ArrivalTime.get(activeprocessindex)!=time) {
		    			WaitingTime.set(activeprocessindex,WaitingTime.get(activeprocessindex)+contextswitch);
		    			}
		    			//delete it from waiting queue
		   				waitingqueueindex.remove(idxhpro);
	    				waitingqueueremainingtime.remove(idxhpro);
	    				waitingqueuepriority.remove(idxhpro);
		    		    
		    		}
		    		// if it is empty here i am just gonna wait till a process came 
		    		else {
		    			activeprocessindex = -1;
		    		}
		    	}
	    		}
		    	/////////////
		    	
		    	//there's a process has a higher priority 
		    	if(!waitingqueueindex.isEmpty()) {
	    			
	    			//get one from waiting queue
	    			int idxhpro = getminidx(waitingqueuepriority);
	    			//current vs waitingqueue
	    			if(priorities.get(activeprocessindex)>waitingqueuepriority.get(idxhpro)) {
	    			int minidx = waitingqueueindex.get(idxhpro);
	    			
	    			////add context switch time to old one 
	    			WaitingTime.set(activeprocessindex,WaitingTime.get(activeprocessindex)+contextswitch);
	    			
	    			//add the curr to waiting queue 
	    			waitingqueueindex.add(activeprocessindex);
	    			waitingqueueremainingtime.add(remainingtimelist.get(activeprocessindex));
	    			waitingqueuepriority.add(remainingtimelist.get(activeprocessindex));
	    			
	    			activeprocessindex = minidx;
	    			
	    			//add context switch time to new one 
	    			WaitingTime.set(activeprocessindex,WaitingTime.get(activeprocessindex)+contextswitch);
	    			
	    			//delete it from waiting queue
	   				waitingqueueindex.remove(idxhpro);
					waitingqueueremainingtime.remove(idxhpro);
					waitingqueuepriority.remove(idxhpro);
	    			}
	    		    
	    		}
		    	
		    	
		    	//get order of arr
		    	if(activeprocessindex!=-1) {
		    	if(Order.isEmpty()) {
					 Order.add(processesname.get(activeprocessindex));
		        	 }
		        	 else {
		        		 if(!Order.get(Order.size()-1).equals(processesname.get(activeprocessindex))) {
		        			 Order.add(processesname.get(activeprocessindex));
		        		 }
		        	 }
		    	}
		        	 System.out.println(processesname.get(activeprocessindex));
		        	 for(int i=0;i<processesnumber;i++) {
		        		 if((ArrivalTime.get(i) <= time) && (i!=activeprocessindex) && (remainingtimelist.get(i)!=0)) {
		        			 //System.out.print(i);
		        			 WaitingTime.set(i, WaitingTime.get(i)+1);
		        		 }
		        	 }
		        	 
		        	 
		 	    if(activeprocessindex!=-1) { 	 
		    	remainingtimelist.set(activeprocessindex, remainingtimelist.get(activeprocessindex)-1);
		 	    	}
		    	time++;
		    	
			}
			
			for(int i=0;i<processesnumber;i++) {
				turnaroundtime.set(i, WaitingTime.get(i)+BurstTime.get(i));
			}
			int waitingsum=0;
			int turnaroundtimesum = 0;
			for(int i=0;i<processesnumber;i++) {
			      waitingsum+=WaitingTime.get(i);
			      turnaroundtimesum+=turnaroundtime.get(i);
			}
			averagewaitingtime = (double)waitingsum/(double)processesnumber;
			averageturnaroundtime = (double)turnaroundtimesum/(double)processesnumber;
		}
		
		
		boolean checkzeros(ArrayList<Integer> a) {
			for(int i:a) {
				if(i!=0) {
					return false;
				}
			}
			return true;
		}
		public void printarrstr(ArrayList<String> a) {
			for(int i=0;i<a.size();i++) {
				System.out.print(a.get(i) + "   ");
			}
			System.out.println();
		}
		public void printarrint(ArrayList<Integer> a) {
			for(int i=0;i<a.size();i++) {
				System.out.print(a.get(i) + "   ");
			}
			System.out.println();
		}
		
		public int getminidx(ArrayList<Integer> a){
			int min = 0;
			for(int i=0;i<a.size();i++) {
				if(a.get(i)<a.get(min)){
					min = i;
				}
			}
			return min;
		}
		public int getfirst(){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			ArrayList<Integer> temppriority = new ArrayList<Integer>();

			int firstarrivaltimeidx = getminidx(ArrivalTime);
			int firstarrivaltime = ArrivalTime.get(firstarrivaltimeidx);
			for(int i=0;i<ArrivalTime.size();i++) {
				if(firstarrivaltime == ArrivalTime.get(i)) {
					temp.add(i);
				}
			}
			for(int i=0;i<temp.size();i++) {
				temppriority.add(priorities.get(temp.get(i)));
			}
			int minpriorityidx = getminidx(temppriority);
			return temp.get(minpriorityidx);
		}
		
	}



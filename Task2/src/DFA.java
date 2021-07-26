import java.util.ArrayList;
import java.util.Arrays;

public class DFA {
    static String [] accepted;
    static String [] zero;
    static String [] one;
    static String [] epsilon;
    static ArrayList<String> state;
    static ArrayList<ArrayList<String>> finalStates;
    static ArrayList<ArrayList<String>> closure;
    //////////////////dfa
    static ArrayList<ArrayList<String>> zeroDfa;
    static ArrayList<ArrayList<String>> oneDfa;
    static ArrayList<ArrayList<String>> stateDfa;
    
	public  DFA (String NFA) {
		state = new ArrayList<String>();
		finalStates = new ArrayList<ArrayList<String>>();
		closure = new ArrayList<ArrayList<String>>();
		zeroDfa = new ArrayList<ArrayList<String>>();
		oneDfa = new ArrayList<ArrayList<String>>();
		stateDfa = new ArrayList<ArrayList<String>>();
		String [] subDfa = NFA.split("\\#");
		zero = subDfa[0].split(";");
		one = subDfa[1].split(";");
		epsilon = subDfa[2].split(";");
		accepted = subDfa[3].split(",");
		
		getStates();
		
		epsilonClosure();
		
		ArrayList<String> initial = closure.get(state.indexOf("0"));
		finalStates.add(initial);
		zeroDfa.add(Trans(zero,initial));
		System.out.println(zeroDfa.get(0));
		oneDfa.add(Trans(one,initial));	
		stateDfa.add(initial);
		if(!search(finalStates,zeroDfa.get(0))&& zeroDfa.get(0).size()!=0) 
		{
			System.out.println(zeroDfa.get(0));
			finalStates.add(zeroDfa.get(0));
		}
		if(!search(finalStates,oneDfa.get(0))&& oneDfa.get(0).size()!=0) 
		{
			finalStates.add(oneDfa.get(0));
		}
	  
	    
		for (int i = 0 ; i < finalStates.size() ; i++)
		{
			if(!search(stateDfa,finalStates.get(i))&& finalStates.get(i).size()!=0) 
			{
				zeroDfa.add(Trans(zero,finalStates.get(i)));
				oneDfa.add(Trans(one,finalStates.get(i)));	
				stateDfa.add(finalStates.get(i));
				for (int j = 0; j<oneDfa.size() ; j++) {
					if(!search(finalStates,oneDfa.get(j))&& oneDfa.get(j).size()!=0) 
					{
						finalStates.add(oneDfa.get(j));
					}
				}
				for (int j = 0; j<zeroDfa.size() ; j++) {
					if(!search(finalStates,zeroDfa.get(j))&& zeroDfa.get(j).size()!=0) 
					{
						finalStates.add(zeroDfa.get(j));
					}
				}
				
			}
		}
}
	
    public static boolean Run (String x ) {
    	 boolean accept = false;    
		 ArrayList<String> element =new ArrayList<String> ();
		 int index = 0;
		 if (x.equals("")) {
    		 accept =checkGoal(stateDfa.get(0));
    	 }
		 for (int i = 0 ; i< x.length() ;i++)
			{
			  if(i==0)
			     {
				     element = stateDfa.get(0);
				     accept =checkGoal(element);
			    	 if (x.charAt(i)=='0') 
			    	 {
			    		 element = zeroDfa.get(0);
			    		 if(element.size()==0)
			    			 return false;
			    		 accept =checkGoal(element);
			    		 index = stateDfa.indexOf(element);
			    	 }
			    	 else if (x.charAt(i)=='1')
			    	 {
			    		 element = oneDfa.get(0);
			    		 if(element.size()==0)
			    			 return false;
			    		 accept =checkGoal(element);
			    		 index = stateDfa.indexOf(element);
			    	 }
			    	 
			     }
			  else 
			     {

			    	 if (x.charAt(i)=='0') 
			    	 {
			    		 element = zeroDfa.get(index);
			    		 if(element.size()==0)
			    			 return false;
			    		 index = stateDfa.indexOf(element);
			    		 accept =checkGoal(element);
			    	 }
			    	 else if (x.charAt(i)=='1')
			    	 {
			    		 element = oneDfa.get(index);
			    		 if(element.size()==0)
			    			 return false;
			    		 index = stateDfa.indexOf(element);
			    		 accept =checkGoal(element);
			    	 }
			     }
			}
    	
		return accept;
    	
    }
	
    public static boolean checkGoal(ArrayList<String> x)
	 {
	    for (int i = 0 ; i < accepted.length ; i++) {
	    	for(int j = 0 ; j<x.size() ; j++) {
	    		String m =""+ x.get(j);
	 	      if (accepted[i].equals(m))
	 		   return true;
	    }}
	    return false;
	 }
    
	public static boolean search(ArrayList<ArrayList<String>> x , ArrayList<String> y) {
		boolean found = false;
		   for (int i = 0 ; i < x.size() ; i++) {
			   found = false;
			   for (int j = 0 ; j < x.get(i).size() ; j++) {
				   for (int k = 0 ; k < y.size() ; k++) {
					  if(x.get(i).equals(y))
						  return true;
				   }
			   }
		   }
			return false;
		}
	

	public static ArrayList<String> Trans(String[] x,ArrayList<String> y) {
		ArrayList<String> z = new ArrayList<String>();
	    for (int i = 0 ; i< x.length ; i++) {
	    	String [] yarab = x[i].split(","); 
	    	for (int j = 0 ; j < y.size() ; j++) {
	    		String n = "" + y.get(j);
	    	if (yarab[0].equals(n)){
	    		z.add(yarab[1]);
	    		z.addAll(closure.get(state.indexOf(yarab[1])));
	    		z= removeDuplicates(z);
	    		
	    	}}}
		return z;
	}

	public static ArrayList<String> removeDuplicates(ArrayList<String> list)
    {
  
        // Create a new ArrayList
        ArrayList<String> newList = new ArrayList<String>();
  
        // Traverse through the first list
        for (int i = 0 ; i < list.size() ; i++ ) {
  
            // If this element is not present in newList
            // then add it
            if (!newList.contains(list.get(i))) {
  
                newList.add(list.get(i));
            }
        }
  
        // return the new list
        return newList;
    }
	public static void epsilonClosure() {
		    ArrayList<String> x = new ArrayList<String>();
		for (int i = 0 ; i < state.size();i++) {
			x = new ArrayList<String>();
			x.add(state.get(i));
			for (int j = 0; j < epsilon.length ;j++ ) {
				String n = "" + epsilon[j].split(",")[0];
				if (n.equals(state.get(i))) {
					if(!(x.contains(epsilon[j].split(",")[1])))
				       x.add(epsilon[j].split(",")[1]); 
				}
				
			}
				for (int m = 1 ; m < x.size() ; m++) {
					String n = x.get(m);
					for (int k = 0; k < epsilon.length ;k++ ) {
						String s = "" + epsilon[k].split(",")[0];
						if (s.equals(n)) {
							if(!(x.contains(epsilon[k].split(",")[1])))
							    x.add(epsilon[k].split(",")[1]); 
						}
					}
				}
				closure.add(x);
		}
	
		
	}
	public static void getStates() {
		boolean found = false;
		String [] x = zero[0].split(",");
		state.add("" + x[0]);
		for (int i = 1 ; i< zero.length ; i++) 
		{
			found = false;
			for (int j = 0 ; j < state.size() ; j++ ) {
				String n = "" + zero[i].split(",")[0];
				
				if (n.equals(state.get(j)))
					found = true;
			}
			if (!found)
			 state.add("" + zero[i].split(",")[0]);
		}
		for (int i = 0 ; i< one.length ; i++) 
		{
			found = false;
			for (int j = 0 ; j < state.size() ; j++ ) {
				String n = "" + one[i].split(",")[0];
				if (n.equals(state.get(j)))
					found = true;
			}
			if (!found)
			 state.add("" + one[i].split(",")[0]);
		}
		for (int i = 0 ; i< epsilon.length ; i++) 
		{
			found = false;
			for (int j = 0 ; j < state.size() ; j++ ) {
				String n = "" + epsilon[i].split(",")[0];
				if (n.equals(state.get(j)))
					found = true;
			}
			if (!found)
			 state.add("" + epsilon[i].split(",")[0]);
		}
		for (int i = 0 ; i< accepted.length ; i++) 
		{
			found = false;
			for (int j = 0 ; j < state.size() ; j++ ) {
				String n = "" + accepted[i];
				if (n.equals(state.get(j)))
					found = true;
			}
			if (!found)
			 state.add("" + accepted[i]);
		}
	}
	public static void main(String[] args) {
		
		
//	   DFA a = new DFA("1,2;7,8#4,5;5,6#0,1;0,3;2,9;3,4;3,7;6,4;6,7;8,9#9");
//   	System.out.println(Run("0"));
//   	System.out.println(Run("110"));
//   	System.out.println(Run("11110"));
//   	System.out.println(Run("01"));
//   	System.out.println(Run("111"));
//   
   	
   	DFA a = new DFA("2,3#3,4;8,9;10,11#0,1;0,8;1,2;1,5;4,7;5,6;6,7;7,1;7,8;9,10;9,12;11,10;11,12#12");
   	System.out.println(Run("011"));
   	System.out.println(Run("0111"));
   	System.out.println(Run("0110"));
   	System.out.println(Run("1"));
   	System.out.println(Run("0101"));
		
		
	}
}

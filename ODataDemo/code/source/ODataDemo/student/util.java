package ODataDemo.student;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.List;
import java.util.Map;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmLiteral;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataMessageException;
import org.apache.olingo.odata2.api.uri.UriParser;
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression;
import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;
import org.apache.olingo.odata2.api.uri.expression.ExpressionVisitor;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.LiteralExpression;
import org.apache.olingo.odata2.api.uri.expression.MemberExpression;
import org.apache.olingo.odata2.api.uri.expression.MethodExpression;
import org.apache.olingo.odata2.api.uri.expression.MethodOperator;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderExpression;
import org.apache.olingo.odata2.api.uri.expression.PropertyExpression;
import org.apache.olingo.odata2.api.uri.expression.SortOrder;
import org.apache.olingo.odata2.api.uri.expression.UnaryExpression;
import org.apache.olingo.odata2.api.uri.expression.UnaryOperator;
// --- <<IS-END-IMPORTS>> ---

public final class util

{
	// ---( internal utility methods )---

	final static util _instance = new util();

	static util _newInstance() { return new util(); }

	static util _cast(Object o) { return (util)o; }

	// ---( server methods )---




	public static final void createSQLFilter (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(createSQLFilter)>> ---
		// @sigtype java 3.5
		// [i] field:0:optional $filter
		// [i] field:0:optional $select
		// [o] field:0:required sqlQuery
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	$filter = IDataUtil.getString( pipelineCursor, "$filter" );
			String	$select = IDataUtil.getString( pipelineCursor, "$select" );
		pipelineCursor.destroy();
		
		if ($select == null || $select.trim().contentEquals("")){
			$select  = "SELECT id, firstName, lastName, email, addressline1,addressline2,city,state,zipcode,mobile FROM demodata.student  ";
		} else{
			$select  = "SELECT "+$select+" FROM demodata.student  ";
		}
		
		if ($filter == null || $filter.trim().contentEquals("")){
			$filter = "";
		}else{
			//$filter.tri
			 FilterExpression expression;
			try {
				expression = UriParser.parseFilter(null, null,$filter );
				 String whereClause = 
						 (String) expression.accept(
								 new ExpressionVisitor(){
									 @Override
										public Object visitBinary(BinaryExpression binaryExpression, BinaryOperator operator, Object leftSide, Object rightSide) {
											// TODO Auto-generated method stub
											 //Transform the OData filter operator into an equivalent sql operator
										    String sqlOperator = "";
										    switch (operator) {
										    case EQ:
										      sqlOperator = "=";
										      break;
										    case NE:
										      sqlOperator = "<>";
										      break;
										    case OR:
										      sqlOperator = "OR";
										      break;
										    case AND:
										      sqlOperator = "AND";
										      break;
										    case GE:
										      sqlOperator = ">=";
										      break;
										    case GT:
										      sqlOperator = ">";
										      break;
										    case LE:
										      sqlOperator = "<=";
										      break;
										    case LT:
										      sqlOperator = "<";
										      break;
										    default:
										      //Other operators are not supported for SQL Statements
										   
										    }  
										    //return the binary statement
										    return leftSide + " " + sqlOperator + " " + rightSide;
										}
		
										@Override
										  public Object visitFilterExpression(FilterExpression filterExpression, String expressionString, Object expression) {
										    System.out.println( "filterExpression "  + filterExpression.toString() )  ;
											return "WHERE " + expression;
										  }
		
										@Override
										public Object visitLiteral(LiteralExpression literal, EdmLiteral edmLiteral) {
											System.out.println( "literal" + literal.toString() + literal.getKind().toString());
										  if(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance().equals(edmLiteral.getType())) {
										    // we have to be carefull with strings due to sql injection
										    // TODO: Prevent sql injection via escaping
										    return "'" + edmLiteral.getLiteral() + "'";
										  } else {
										    return "'" + edmLiteral.getLiteral() + "'";
										  }
										}
										@Override
										public Object visitMember(MemberExpression arg0, Object arg1, Object arg2) {
											// TODO Auto-generated method stub
											return null;
										}
		
										@Override
										public Object visitMethod(MethodExpression arg0, MethodOperator arg1, List<Object> arg2) {
											// TODO Auto-generated method stub
											//Implement mapping between ODATA methods to SQL server functions
											String returnStr =   arg1.toString() + "(";
											for(Object param : arg2) {
												returnStr = returnStr + param + ",";
											}
											returnStr = returnStr.substring(0,returnStr.length()-1) + ")";
											return returnStr;
											
										}
		
										@Override
										public Object visitOrder(OrderExpression arg0, Object arg1, SortOrder arg2) {
											// TODO Auto-generated method stub
											
											return arg1.toString();
										}
		
										@Override
										public Object visitOrderByExpression(OrderByExpression arg0, String arg1, List<Object> arg2) {
											// TODO Auto-generated method stub
											return null;
										}
		
										@Override
										public Object visitProperty(PropertyExpression propertyExpression, String uriLiteral, EdmTyped edmProperty) {
										  if (edmProperty == null) {
										    //If a property is not found it wont be represented in the database thus we have to throw an exception
											  return uriLiteral;
										  } else {
										    //It is also possible to use the mapping of the edmProperty if the name differs from the databasename
										    try {
										      return edmProperty.getName();
										    } catch (EdmException e) {
										      //throw new Exception(e);
										    	return  "";
										    }
										  }
										}
		
										@Override
										public Object visitUnary(UnaryExpression arg0, UnaryOperator arg1, Object arg2) {
											// TODO Auto-generated method stub
											return null;
										}
										  
					 
				 });
				   
				    $filter=  whereClause;
			
			} catch (ODataMessageException | ODataApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
		}
		$select = $select +" " + $filter;
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "sqlQuery", $select );
		pipelineCursor_1.destroy();
			
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	class JdbcStringVisitor implements ExpressionVisitor {
	
		@Override
		public Object visitBinary(BinaryExpression binaryExpression, BinaryOperator operator, Object leftSide, Object rightSide) {
			// TODO Auto-generated method stub
			 //Transform the OData filter operator into an equivalent sql operator
		    String sqlOperator = "";
		    switch (operator) {
		    case EQ:
		      sqlOperator = "=";
		      break;
		    case NE:
		      sqlOperator = "<>";
		      break;
		    case OR:
		      sqlOperator = "OR";
		      break;
		    case AND:
		      sqlOperator = "AND";
		      break;
		    case GE:
		      sqlOperator = ">=";
		      break;
		    case GT:
		      sqlOperator = ">";
		      break;
		    case LE:
		      sqlOperator = "<=";
		      break;
		    case LT:
		      sqlOperator = "<";
		      break;
		    default:
		      //Other operators are not supported for SQL Statements
		   
		    }  
		    //return the binary statement
		    return leftSide + " " + sqlOperator + " " + rightSide;
		}
	
		@Override
		  public Object visitFilterExpression(FilterExpression filterExpression, String expressionString, Object expression) {
		      return "WHERE " + expression;
		  }
	
		@Override
		public Object visitLiteral(LiteralExpression literal, EdmLiteral edmLiteral) {
		  if(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance().equals(edmLiteral.getType())) {
		    // we have to be carefull with strings due to sql injection
		    // TODO: Prevent sql injection via escaping
		    return "'" + edmLiteral.getLiteral() + "'";
		  } else {
		    return "'" + edmLiteral.getLiteral() + "'";
		  }
		}
		@Override
		public Object visitMember(MemberExpression arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Object visitMethod(MethodExpression arg0, MethodOperator arg1, List<Object> arg2) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Object visitOrder(OrderExpression arg0, Object arg1, SortOrder arg2) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Object visitOrderByExpression(OrderByExpression arg0, String arg1, List<Object> arg2) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Object visitProperty(PropertyExpression arg0, String arg1, EdmTyped arg2) {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Object visitUnary(UnaryExpression arg0, UnaryOperator arg1, Object arg2) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	// --- <<IS-END-SHARED>> ---
}


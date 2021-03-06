package my.inventive.grocery_partner.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import my.inventive.grocery_partner.R;
import my.inventive.grocery_partner.modules.Order;

public class ShipeedAdapter extends RecyclerView.Adapter<ShipeedAdapter.ShipeedAdapterViewHolder>
{
    Context context;
    ArrayList<Order> al;

    public ShipeedAdapter (Context context, ArrayList<Order> al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public ShipeedAdapter.ShipeedAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.shipeed_fragment_card,parent,false);
        return new ShipeedAdapter.ShipeedAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShipeedAdapter.ShipeedAdapterViewHolder holder, int position)
    {
        final Order o=al.get(position);
        holder.orderDate.setText(o.getDateOfOrder());
        holder.address.setText(o.getAddress());
        holder.name.setText(o.getName());
        holder.itemCount.setText(o.getItemName());
        holder.mobile.setText(o.getMobile());
        holder.totalPrice.setText(o.getTotalAmount());
        holder.orderId.setText(o.getOrderId());
        holder.orderStatus.setText(o.getOrderStatus());
        holder.orderTime.setText(o.getTimeOfOrder());
        holder.itemId.setText(o.getItemId());
        holder.confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle("are you sure you want to out this order for delivery").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                        final ProgressDialog pd = new ProgressDialog(context);
                        pd.setTitle("Pet Seller");
                        pd.setMessage("Loading...");
                        pd.show();
                        String url = "https://inventivepartner.com/petmart/changeOrderStatus.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pd.dismiss();
                                if(response.equals("Status Updated"))
                                {
                                    Toast.makeText(context, "Order Out For Delivery", Toast.LENGTH_SHORT).show();
                                    al.remove(o);
                                    notifyDataSetChanged();
                                }
                                else Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pd.dismiss();
                                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("id", o.getId());
                                map.put("status","Out_For_Delivery");
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context, new HurlStack());
                        requestQueue.add(stringRequest);

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                }).show();
            }
        });
        holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle("are you sure you want to cancel this order").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                        final ProgressDialog pd = new ProgressDialog(context);
                        pd.setTitle("Pet Seller");
                        pd.setMessage("Loading...");
                        pd.show();
                        String url = "https://inventivepartner.com/petmart/changeOrderStatus.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pd.dismiss();
                                if(response.equals("Status Updated"))
                                {
                                    Toast.makeText(context, "Order Cancled", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                                else Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pd.dismiss();
                                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("id", o.getId());
                                map.put("status","Cancled");
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context, new HurlStack());
                        requestQueue.add(stringRequest);

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }


    public class ShipeedAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate,orderTime,orderId,orderStatus,name,address,mobile,itemCount,totalPrice,itemId;
        Button confirmOrder,cancelOrder;
        public ShipeedAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate=itemView.findViewById(R.id.order_date);
            orderTime=itemView.findViewById(R.id.order_time);
            orderId=itemView.findViewById(R.id.orderId);
            orderStatus=itemView.findViewById(R.id.status);
            name=itemView.findViewById(R.id.name);
            address=itemView.findViewById(R.id.address);
            mobile=itemView.findViewById(R.id.mobile);
            itemCount=itemView.findViewById(R.id.total_savings);
            totalPrice=itemView.findViewById(R.id.payableAmount);
            cancelOrder=itemView.findViewById(R.id.cancelOrder);
            confirmOrder=itemView.findViewById(R.id.proceedOrder);
            itemId=itemView.findViewById(R.id.itemId);
        }
    }
}

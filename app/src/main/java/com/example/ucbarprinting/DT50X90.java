package com.example.ucbarprinting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

import ZPL.IPort;
import ZPL.PublicFunction;
import ZPL.ZPLPrinterHelper;

public class DT50X90 extends AppCompatActivity {

    private Context thisCon=null;
    private UsbManager mUsbManager=null;
    private UsbDevice device=null;
    private static final String ACTION_USB_PERMISSION = "com.HPRTSDKSample";
    private PendingIntent mPermissionIntent=null;
    private static IPort Printer=null;
    private ZPLPrinterHelper zplPrinterHelper;

    Button connect_btn,print_btn;
    EditText itemname_txt,size_txt,type_txt,core_txt,roll_txt,copy_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dt50_x90);

        try
        {
            thisCon=this.getApplicationContext();

            connect_btn=findViewById(R.id.connect_btn);
            print_btn=findViewById(R.id.print_btn);

            itemname_txt=findViewById(R.id.itemname_txt);
            size_txt=findViewById(R.id.size_txt);
            type_txt=findViewById(R.id.type_txt);
            core_txt=findViewById(R.id.core_txt);
            roll_txt=findViewById(R.id.roll_txt);
            copy_txt=findViewById(R.id.copies_txt);
            mPermissionIntent = PendingIntent.getBroadcast(thisCon, 0, new Intent(ACTION_USB_PERMISSION), 0);
            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            thisCon.registerReceiver(mUsbReceiver, filter);

            zplPrinterHelper = ZPLPrinterHelper.getZPL(thisCon);
        }
        catch (Exception e)
        {
            Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> onCreate ")).append(e.getMessage()).toString());
        }

        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//				HPRTPrinter=new ZPLPrinterHelper(thisCon,arrPrinterList.getItem(spnPrinterList.getSelectedItemPosition()).toString());
                //USB not need call "iniPort"
                mUsbManager = (UsbManager) thisCon.getSystemService(Context.USB_SERVICE);
                HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
                Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

                boolean HavePrinter=false;
                while(deviceIterator.hasNext())
                {
                    device = deviceIterator.next();
                    int count = device.getInterfaceCount();
                    for (int i = 0; i < count; i++)
                    {
                        UsbInterface intf = device.getInterface(i);
                        if (intf.getInterfaceClass() == 7)
                        {
                            HavePrinter=true;
                            mUsbManager.requestPermission(device, mPermissionIntent);
                        }
                    }
                }
                if(!HavePrinter)
                    Toast.makeText(getApplicationContext(),"NO printer",Toast.LENGTH_LONG).show();
            }
        });

        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintPrn();
            }
        });
    }

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            try
            {
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action))
                {
                    synchronized (this)
                    {
                        device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                        {
                            if(zplPrinterHelper.PortOpen(device)!=0)
                            {
//				        		HPRTPrinter=null;

                                return;
                            }

                        }
                        else
                        {
                            return;
                        }
                    }
                }
                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
                {
                    device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null)
                    {
                        zplPrinterHelper.PortClose();
                    }
                }
            }
            catch (Exception e)
            {
                Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> mUsbReceiver ")).append(e.getMessage()).toString());
            }
        }
    };

    private void PrintPrn()
    {
        String inputitemname=itemname_txt.getText().toString();
        String inputsize=size_txt.getText().toString();
        String inputtype=type_txt.getText().toString();
        String inputcore=core_txt.getText().toString();
        String inputroll=roll_txt.getText().toString();
        String inputcopy=copy_txt.getText().toString();
        try
        {
            String prn="";

            prn=prn+"<xpml><page quantity='0' pitch='90.1 mm'></xpml>^XA"+"\n";
            prn=prn+"^SZ2^JMA"+"\n";
            prn=prn+"^MCY^PMN"+"\n";
            prn=prn+"^PW401"+"\n";
            prn=prn+"^JZY"+"\n";
            prn=prn+"^LH0,0^LRN"+"\n";
            prn=prn+"^XZ"+"\n";
            prn=prn+"<xpml></page></xpml><xpml><page quantity='1' pitch='90.1 mm'></xpml>^XA"+"\n";
            prn=prn+"^FT47,690"+"\n";
            prn=prn+"^CI0"+"\n";
            prn=prn+"^A0B,37,47^FD"+inputitemname+"^FS"+"\n";
            prn=prn+"^A0B,37,47^FDSize :^FS"+"\n";
            prn=prn+"^FT195,690"+"\n";

            prn=prn+"^A0B,37,47^FDType :^FS"+"\n";
            prn=prn+"^FT267,690"+"\n";
            prn=prn+"^A0B,37,47^FDCore :^FS"+"\n";
            prn=prn+"^FT339,690"+"\n";
            prn=prn+"^A0B,37,47^FDRoll Qty :^FS"+"\n";
            prn=prn+"^FT123,577"+"\n";
            prn=prn+"^A0B,37,47^FD"+inputsize+"^FS"+"\n";
            prn=prn+"^FT195,563"+"\n";
            prn=prn+"^A0B,37,47^FD"+inputtype+"^FS"+"\n";

            prn=prn+"^FT267,557"+"\n";
            prn=prn+"^A0B,37,47^FD"+inputcore+"^FS"+"\n";
            prn=prn+"^FT339,491"+"\n";
            prn=prn+"^A0B,37,47^FD"+inputroll+"^FS"+"\n";
            prn=prn+"^PQ"+inputcopy+",0,"+inputcopy+",Y"+"\n";
            prn=prn+"^XZ"+"\n";
            prn=prn+"<xpml></page></xpml><xpml><end/></xpml>";

            //Toast.makeText(getApplicationContext(),prn,Toast.LENGTH_LONG).show();
            zplPrinterHelper.printData(prn);
        }
        catch(Exception e)
        {
            Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> PrintSampleReceipt ")).append(e.getMessage()).toString());
        }

    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), SelectSize.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(myIntent);
    }
}
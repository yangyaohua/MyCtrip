package com.example.fragment;

import java.util.List;

import com.example.activity.SearchTicketActivity;
import com.example.activity.ChangePasswdActivity;
import com.example.adapter.MainAdapter;
import com.example.myctrip.R;
import com.example.utils.Constants;
import com.example.utils.SpUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainFragment extends Fragment {
	private GridView gv_main;
	private List<String> list; 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		initView(view);
		initEvent();
		return view;
	}
	
	private void initEvent() {
		gv_main.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			switch (position) {
			case 0:
				Intent intent0 = new Intent(getActivity(),SearchTicketActivity.class);
				startActivity(intent0);
				break;
		/*	case 1:
				Intent intent1 = new Intent(MainActivity.this, NoteActivity.class);
				startActivity(intent1);
				break;*/
			case 2:
				Intent intent2 = new Intent(getActivity(),ChangePasswdActivity.class);
				startActivity(intent2);
				break;
			case 3:
				new AlertDialog.Builder(getActivity())
					.setTitle("退出登录")
					.setMessage("亲，真的要退出登录吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							SpUtil.put(getActivity(), Constants.LOGIN_STATE, false);
							SpUtil.put(getActivity(), Constants.PC_SESSIONID, "");
							Toast.makeText(getActivity(), "退出成功", 2).show();
							getActivity().finish();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					})
					.create().show();
				break;
			default:
				break;
			}
		}
	});
	}

	private void initView(View view) {
		gv_main = (GridView) view.findViewById(R.id.gv_main);
		gv_main.setAdapter(new MainAdapter(getActivity()));
	}
}

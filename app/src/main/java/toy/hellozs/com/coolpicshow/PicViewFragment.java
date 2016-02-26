package toy.hellozs.com.coolpicshow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import toy.hellozs.com.coolpicshow.bean.Pic;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2015/12/12.
 */
public class PicViewFragment extends Fragment {
    public static final String POSITION = "position";
    private static final String ARG_PIC_NUMBER = "pic_number";
    private Pic pic;

    public PicViewFragment() {
    }

    public static PicViewFragment newInstance(int pic_number) {
        PicViewFragment fragment = new PicViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PIC_NUMBER, pic_number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int i = getArguments().getInt(ARG_PIC_NUMBER);
        pic = ((PicViewActivity) getActivity()).getPic(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        int c = ((PicViewActivity) getActivity()).getPicCount();
        getActivity().setTitle(pic.getTitle() + " 【" + c + "P】");
    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pic_view, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.id_pic);
        TextView textView = (TextView) rootView.findViewById(R.id.id_pic_content);
        Picasso.with(getActivity()).load(pic.getUrl()).error(R.drawable.ic_menu_gallery).into(imageView);
        textView.setText(pic.getContent());
        return rootView;
    }
}

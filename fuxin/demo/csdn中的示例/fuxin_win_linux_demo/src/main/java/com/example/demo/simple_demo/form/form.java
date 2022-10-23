// Copyright (C) 2003-2022, Foxit Software Inc..
// All Rights Reserved.
//
// http://www.foxitsoftware.com
//
// The following code is copyrighted and contains proprietary information and trade secrets of Foxit Software Inc..
// You cannot distribute any part of Foxit PDF SDK to any third party or general public,
// unless there is a separate license agreement with Foxit Software Inc. which explicitly grants you such rights.
//
// This file contains an example to demonstrate how to use Foxit PDF SDK to add form field and
// get form field information.

import com.foxit.sdk.pdf.interform.Form;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.annots.Annot;
import com.foxit.sdk.pdf.annots.DefaultAppearance;
import com.foxit.sdk.pdf.annots.Widget;
import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.Font;
import com.foxit.sdk.common.Library;
import com.foxit.sdk.common.WStringArray;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.interform.ChoiceOptionArray;
import com.foxit.sdk.pdf.interform.Control;
import com.foxit.sdk.pdf.interform.Field;
import com.foxit.sdk.pdf.interform.ChoiceOption;
import com.foxit.sdk.pdf.actions.*;
import com.foxit.sdk.common.Constants;
import java.io.File;

public class form {

	static void AddInteractiveForms(PDFPage page, Form form) {
		{
			// Add push button field.
			Control btn_submit = null;
			try {
				btn_submit = form.addControl(page, "Push Button Submit", Field.e_TypePushButton,
						new RectF(50, 750, 150, 780));
			} catch (PDFException e) {
				e.printStackTrace();
			}
			// Set default Appearance
			DefaultAppearance default_ap = new DefaultAppearance();
			default_ap.setFlags(DefaultAppearance.e_FlagFont | DefaultAppearance.e_FlagFontSize
					| DefaultAppearance.e_FlagTextColor);
			try {
				default_ap.setFont(new Font(Font.e_StdIDHelveticaB));
			} catch (PDFException e) {
				e.printStackTrace();
			}
			default_ap.setText_size(12f);
			default_ap.setText_color(0x000000);
			try {
				form.setDefaultAppearance(default_ap);
			} catch (PDFException e) {
				e.printStackTrace();
			}

			// Set push button appearance.
			Widget widget = null;
			try {
				widget = btn_submit.getWidget();
				widget.setHighlightingMode(Annot.e_HighlightingPush);
				widget.setMKBorderColor(0xFF0000);
				widget.setMKBackgroundColor(0xF0F0F0);
				widget.setMKNormalCaption("Submit");
				widget.resetAppearanceStream();
			} catch (PDFException e) {
				e.printStackTrace();
			}

			// Set submit form action.
			try {
				SubmitFormAction submit_action = new SubmitFormAction(Action.create(form.getDocument(), Action.e_TypeSubmitForm));
				int count = form.getFieldCount(null);
				WStringArray name_array = new WStringArray();
				for (int i = 0; i < count; i++) {
					name_array.add(form.getField(i, null).getName());
				}

				submit_action.setFieldNames(name_array);
				submit_action.setURL("http://www.foxitsoftware.com");
				widget.setAction(submit_action);
			} catch (PDFException e) {
				e.printStackTrace();
			}

			System.out.println("Add button field.");
		}

		{
			// Add radio button group
			try {
				Control control = form.addControl(page, "Radio Button0", Field.e_TypeRadioButton,
						new RectF(50.0f, 700, 90, 740));
				Control control1 = form.addControl(page, "Radio Button0", Field.e_TypeRadioButton,
						new RectF(100.0f, 700, 140, 740));
				control.setExportValue("YES");
				control.setChecked(true);
				// Update radio button's appearance.
				control.getWidget().resetAppearanceStream();

				control1.setExportValue("NO");
				control1.setChecked(false);
				// Update radio button's appearance.
				control1.getWidget().resetAppearanceStream();
			} catch (PDFException e) {
				e.printStackTrace();
			}

			System.out.println("Add radio button.");
		}

		{
			// Add check box
			try {
				Control control = form.addControl(page, "Check Box0", Field.e_TypeCheckBox,
						new RectF(50.0f, 650, 90, 690));
				control.setChecked(true);
				Widget widget = control.getWidget();
				widget.setMKBorderColor(0x000000);
				widget.setMKBackgroundColor(0xFFFFFF);
				widget.resetAppearanceStream();
			} catch (PDFException e) {
				e.printStackTrace();
			}

			System.out.println("Add check box.");
		}

		{
			// Add text field
			try {
				Control control = form.addControl(page, "Text Field0", Field.e_TypeTextField, new RectF(50, 600, 90, 640));
				control.getField().setValue("3");
				// Update text field's appearance.
				control.getWidget().resetAppearanceStream();
				
				Control control1 = form.addControl(page, "Text Field1", Field.e_TypeTextField, new RectF(100, 600, 140, 640));
				control1.getField().setValue("23");
				// Update text field's appearance.
				control1.getWidget().resetAppearanceStream();
				
				Control control2 = form.addControl(page, "Text Field2", Field.e_TypeTextField, new RectF(150, 600, 190, 640));
				Field field2 = control2.getField();
				JavaScriptAction javascipt_action = new JavaScriptAction(
						Action.create(form.getDocument(), Action.e_TypeJavaScript));
				javascipt_action
						.setScript("AFSimple_Calculate(\"SUM\", new Array (\"Text Field0\", \"Text Field1\"));");
				AdditionalAction aa = new AdditionalAction(field2);
				aa.setAction(AdditionalAction.e_TriggerFieldRecalculateValue, javascipt_action);
				field2.setValue("333333");
				// Update text field's appearance.
				control2.getWidget().resetAppearanceStream();
			} catch (PDFException e) {
				e.printStackTrace();
			}

			System.out.println("Add text field.");
		}

		{
			// Add text field with flag comb
			try {
				Control control = form.addControl(page, "Text Field3", Field.e_TypeTextField, new RectF(50, 570, 100, 590));
				Field field = control.getField();
				int flag = Field.e_FlagTextComb;
				field.setFlags(flag);
				field.setValue("94538");
				field.setMaxLength(5);
				// Update text field's appearance.
				control.getWidget().resetAppearanceStream();
			} catch (PDFException e) {
				e.printStackTrace();
			}
		}

		{
			// Add text field, with flag multiline.
			try {
				Control control = form.addControl(page, "Text Field4", Field.e_TypeTextField, new RectF(110, 550, 190, 590));
				Field field = control.getField();
				int flag = Field.e_FlagTextMultiline;
				field.setFlags(flag);
				field.setValue("Text fields are boxes or spaces in which the user can enter text from the keyboard.");
				// Update text field's appearance.
				control.getWidget().resetAppearanceStream();
			} catch (PDFException e) {
				e.printStackTrace();
			}
		}

		{
			// Add text field, with flag password.
			try {
				Control control = form.addControl(page, "Text Field5", Field.e_TypeTextField, new RectF(200, 570, 240, 590));
				Field field = control.getField();
				int flag = Field.e_FlagTextPassword;
				field.setFlags(flag);
				field.setValue("Password");
				// Update text field's appearance.
				control.getWidget().resetAppearanceStream();
			} catch (PDFException e) {
				e.printStackTrace();
			}
		}

		{
			// Add list box
			try {
				Control control = form.addControl(page, "List Box0", Field.e_TypeListBox, new RectF(50, 450, 350, 500));
				Field field = control.getField();
				ChoiceOptionArray options = new ChoiceOptionArray();
				options.add(new ChoiceOption("Foxit SDK", "Foxit SDK", true, true));
				options.add(new ChoiceOption("Foxit Reader", "Foxit Reader", true, true));
				options.add(new ChoiceOption("Foxit Phantom", "Foxit Phantom", true, true));
				field.setOptions(options);

				Widget widget = control.getWidget();
				widget.setMKBorderColor(0x000000);
				widget.setMKBackgroundColor(0xFFFFFF);
				widget.resetAppearanceStream();
			} catch (PDFException e) {
				e.printStackTrace();
			}
		}

		{
			// Add combo box
			Control control = null;
			Field field = null;
			Widget widget = null;

			try {
				control = form.addControl(page, "Combo Box0", Field.e_TypeComboBox, new RectF(50, 350, 350, 400));
				field = control.getField();
				ChoiceOptionArray options = new ChoiceOptionArray();
				options.add(new ChoiceOption("Foxit SDK", "Foxit SDK", true, true));
				options.add(new ChoiceOption("Foxit Reader", "Foxit Reader", true, true));
				options.add(new ChoiceOption("Foxit Phantom", "Foxit Phantom", true, true));
				field.setOptions(options);

				widget = control.getWidget();
				widget.setMKBorderColor(0x000000);
				widget.setMKBackgroundColor(0xFFFFFF);
				widget.resetAppearanceStream();

			} catch (PDFException e) {
				e.printStackTrace();
			}
		}
	}

	private static void createResultFolder(String output_path) {
		File myPath = new File(output_path);
		if (!myPath.exists()) {
			myPath.mkdir();
		}
	}

    // You can also use System.load("filename") instead. The filename argument must be an absolute path name.
    static {
        String os = System.getProperty("os.name").toLowerCase();
        String lib = "fsdk_java_";
        if (os.startsWith("win")) {
            lib += "win";
        } else if (os.startsWith("mac")) {
            lib += "mac";
        } else {
            lib += "linux";
      	}
        if (System.getProperty("sun.arch.data.model").equals("64")) {
            lib += "64";
        } else {
            lib += "32";
        }
        System.loadLibrary(lib);
    }

	public static void main(String[] args) {
		String key = "8f3gFcGNvRsN+jePnKBLSM96wg7jOhOBvjleVQBqxdgEt8yeZv3b/keyDABt7FpC1mIss1nuzpGTASF0ZZbmWzisAu9d0G7Ba8X3zrx4zENZAuYUunko2Ap08ML9LBBEgVu3FqfexKQh38GI6kkyuZ5MFFY1WiaHQ6HjaeSQWIAdeEgu1T5bSmYV3ChsvO9g76olFLcO+kdUSadIp7XyQ4CdsLS7f/hLSUxnuXxlmGUaNBKohWRYq9y+wMVpZM6Kl78zi4dq/lN0kMInM7lAjA/tY6J1x73ZqSkwAXXCMe6ADwbpAi7nvu8kO53wuLsnDEEmUmiYQTw5KqqjCuWY1GcEjjTc+7alM1ExvVY3UDmtNBKqsHIxSoMkyoKaojA4w5FEhPIxDgLZ2ugYK1msZnn+tP696FnUABb44THB+kzpJs2MsCNQGiJiIfetUfJL76DAP//kVi/d/a8HC0kgebvgXH5pqoCAsQ7XKSgCq6Oy1gsTeCuaqPerC/dLMq7jMG6t9b01faCS/HHT9EX0JVCSdOCn4OQqCuC/zwFKblVkGCRS+JJ1SiCszPfwDJGjqDOy51SqHqFQcHDayxCFojAp+Z+cZAls34ot3tzV0Ewveh65dbMpep6qUO+mGAjJmq4OOTSliurssGT6xj061mJR37mZ7QaSO11hz90xCpb7feb5937icy1fq4asfca2bsMEuq2ISe4kYI8Fzy1oFDn5dsItjWo0RC8feeLc2hGzhxKUwGHzHjGaG1jDbcMWWmaqy43e4UdRPHanB8MiygK0CYYWCG8dfK0RF/jauHekgVCXSSE3Qazbxv3NDuYdW4HsV9OdgrEMks1wLTvHyAK60eK0FMpgf/HjlU0FyaR00RzY6icKRSLQy+ffovgM4t3WMUUMaYn/udGioSYY4mZPKaWbxC/2OXenUfdCsFyb6ssAvowvLh+XvmD6pgXf2U9x4gZ0GD3Ynq4WTi6oOtOxsWf0Tmf9tmpULZGoxI3U0wO10ZWVitXsFhDPFZOwy2wFE5QF+JxQozRetpm9X5toiG7vlUu4nh2qMAXLNp4nXww66e/oR27SVDacEkuMwHtsFGHJCCFTkisFL7BehTMel39p0zb3oFnzCk8tUYwpiB/y8MRsfqoYgpI8kUnE2i/y15K+jmtnWbWkJZ3XHRm1vKqxNK3ihDLGgPloEjqpBUPzf1WYyzmdcsOLoOsl9XdB3Ro3DOX3SAmD1vkXc/qTGN+MuSHiMPZbuzCJ8FGOkx2tsFWFkswSBQ//4dxqEaZ2Hh7yx4k6zkLsXGuwcfU3pYX8MbZWdNf4s8uWEzS1VJkTLdoKKx2LSVnZ6yvGThn82ohLbbqVx2sn";
		String sn = "tRs3tntQBkjXIQdh7XzekjZTJZ+lkTbQyBEVrQowtRotaU49uX2EdQ==";
		String output_path = "../output_files/form/";
		createResultFolder(output_path);
		// Initialize library
		int error_code = Library.initialize(sn, key);
		if (error_code != Constants.e_ErrSuccess) {
			System.out.printf("Library Initialize Error: %d\n", error_code);
			return;
		}

		try {
			PDFDoc doc = new PDFDoc();
			Form form = new Form(doc);
			// Create a blank new page and add some form fields.
			PDFPage page = doc.insertPage(0, PDFPage.e_SizeLetter);
			AddInteractiveForms(page, form);

			String newPdf = output_path + "form.pdf";
			doc.saveAs(newPdf, PDFDoc.e_SaveFlagNoOriginal);
		} catch (PDFException e) {
			System.out.println(e.getMessage());
			return;
		}
		Library.release();
	}

}

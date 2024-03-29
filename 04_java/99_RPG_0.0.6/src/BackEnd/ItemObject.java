package BackEnd;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Main.ProcBoard;
import Maps.Data;
import Maps.MapOPj;
import Maps.Maps;
import Maps.Village;
import Selective.ItemOP;
import uitn.Cw;
import uitn.InputS;
import uitn.Material;

public class ItemObject {
	public static int C_money = 0;
	static int sumCI = 0;
	static int sumCI2 = 0;
	public static int itempay;

	// 자산확인
	static public void moneyshow() {
		///////////////////// 자산확인
		Material.line();
		String SQL_C = "select * from charinfo where C_NAME = '" + Data.Main_C_NAME + "';";
		try {
			ProcBoard.result = ProcBoard.st.executeQuery(SQL_C);
			if (ProcBoard.result.next()) { // 데이터가 있는지 확인
				C_money = ProcBoard.result.getInt("C_MONEY");
				System.out.println("내 자산 : " + C_money + " G");
				Material.line();
			} else {
				System.out.println("데이터가 없습니다.");
			}
			ProcBoard.result.close(); // 이전 ResultSet 닫기

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		///////////////////// 자산확인
	}

	// 가방 열기
	// 얘 정상 작동함
	static public void bag() {
		Material.line2();
		System.out.print("[  인벤토리  ]");
		Material.line();
		int C_money = 0;
		System.out.println("용사, " + Data.Main_C_NAME);
		Material.line();
		String SQL_C = "select * from charinfo where C_NAME = '" + Data.Main_C_NAME + "';";
		try {
			ProcBoard.result = ProcBoard.st.executeQuery(SQL_C);
			if (ProcBoard.result.next()) { // 데이터가 있는지 확인
				C_money = ProcBoard.result.getInt("C_MONEY");
				System.out.println("내 자산 " + C_money + " G");
			} else {
				System.out.println("데이터가 없습니다.");
			}
			ProcBoard.result.close(); // 이전 ResultSet 닫기

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			// 명시적으로 ResultSet을 닫음
			if (ProcBoard.result != null) {
				try {
					ProcBoard.result.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		///////////////////// 자산확인
		// 아이템 확인 세트
		Inventory.Inventory_load();
		Character.MAIN_C_find_item_in();
		Inventory.Inventory(); // 아이템확인
		Material.line();
		Cw.Wn("[1] 자세히 보기 |  [2] 버리기  | [SP] 뒤로가기");
		try {
			loop: while (true) {
				int input = System.in.read(); // 현재 키보드 입력 값이 숫자로 변환된 상태
				boolean Sysread = input >= 97 && input <= 122;
				boolean Sysread2 = input >= 65 && input <= 90;
				if (Sysread == true || Sysread2 == true) {
					Cw.Wn("숫자만와 SP만 사용 가능합니다.");
					Cw.Wn("TIP 이동을 원할 시 뒤로가기를 누르세요");
				}
				if (input == 49) {
					String cmd = InputS.r("상세하게 볼 아이템 번호");
					String SQL_item2 = "select * from Inventory where IM_NO =" + cmd;
					try {
						ProcBoard.result = ProcBoard.st.executeQuery(SQL_item2);
						Material.line();
						if (!ProcBoard.result.next()) {
							System.out.println("해당 아이템이 없습니다.");
							Cw.Wn("[1] 자세히 보기 |  [2] 버리기  | [SP] 뒤로가기");
						} else {
							String title = ProcBoard.result.getString("IM_NAME"); // p_name 필드(열) 의 데이터 꺼내기(1개 꺼낸거에서)
							String intno = ProcBoard.result.getString("IM_INT"); // p_name 필드(열) 의 데이터 꺼내기(1개 꺼낸거에서)
							String content = ProcBoard.result.getString("IM_TEXT"); // p_name 필드(열) 의 데이터 꺼내기(1개 꺼낸거에서)
							String price = ProcBoard.result.getString("IM_MONEY"); // p_name 필드(열) 의 데이터 꺼내기(1개 꺼낸거에서)
							System.out.print("아이템명: " + title);
							System.out.println(" X " + intno);
							System.out.println("설명: " + content);
							System.out.println(price + " G");
							Material.line();
							Cw.Wn("[1] 자세히 보기 |  [2] 버리기  | [SP] 뒤로가기");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} finally {
						// 결과 집합을 닫아준다.
						if (ProcBoard.result != null) {
							try {
								ProcBoard.result.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}

				} // "1"
				else if (input == 50) {
					System.out.println("아이템 버리기");
					System.out.println("삭제할 번호를 입력하세요");
					String delno = InputS.r("삭제할 아이템 번호");
					String load = String.format("select * from inventory where IM_NO =" + delno + ";");
					String delname = null;
					String delmy = null;
					try (Statement newStatement = ProcBoard.con.createStatement()) {
						ProcBoard.result = newStatement.executeQuery(load);
						while (ProcBoard.result.next()) {
							// 캐삭빵권 전용 옵션
							String cantdel = ProcBoard.result.getString("IM_NAME");
							if (cantdel.equals("캐삭빵 면제권")) {
								String updateCharinfo2 = String.format(
										"update charinfo set CANTDEL = 0 where C_NAME = '" + Data.Main_C_NAME + "';");
								ProcBoard.st.executeUpdate(updateCharinfo2); // 수정된 부분
							}
							////////////////////////////////////////////////////////////////
							delname = ProcBoard.result.getString("IM_NAME");
							delmy = ProcBoard.result.getString("IM_MY");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					String delname_1 = String.format("delete from item where I_NAME = '%s' and I_MY = '%s';", delname,
							delmy);
					String delnox = String.format("delete from Inventory where IM_NO = '%s';", delno);
					ProcBoard.dbExecuteUpdate(delname_1);
					ProcBoard.dbExecuteUpdate(delnox);
					System.out.println("삭제 종료");
					return;
				} // "2"
				if (input == 32) {
					Maps.run();
					Maps.Mscanner();
					MapOPj.run();
					break loop;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 명시적으로 ResultSet을 닫음
			if (ProcBoard.result != null) {
				try {
					ProcBoard.result.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static public void shop() {
		moneyshow();
		Cw.Wn("[1] 구매  |  [2] 판매  | [SP] 뒤로가기");
		loop: while (true) {
			int input;
			try {
				input = System.in.read();
				// 현재 키보드 입력 값이 숫자로 변환된 상태
				boolean Sysread = input >= 97 && input <= 122;
				boolean Sysread2 = input >= 65 && input <= 90;
				if (Sysread == true || Sysread2 == true) {
					Cw.Wn("숫자만와 SP만 사용 가능합니다.");
					Cw.Wn("TIP 이동을 원할 시 뒤로가기를 누르세요");
				}
				if (input == 32) {
					Maps.move = true;
					Village.run();
					break loop;
				}
				if (input == 49) { // 구매하기
					purchaseItem();
				} // "1"
				else if (input == 50) { // 판매하기
					handleSellOption();
				} // "2"
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	static public void handleSellOption() {
		Material.line2();
		System.out.print("[  인벤토리  ]");
		Material.line();
		System.out.println("내 자산 : " + C_money + " G");
		Material.line();
		Inventory.Inventory_load();
		Character.MAIN_C_find_item_in();
		Inventory.InventoryForpay();
		Material.line();
		System.out.println("판매하기");
		System.out.println("*원하지 않을 시 0번을 입력하세요");
		String delno = InputS.r("판매할 아이템 번호");
		String SQL_item = "select * from Inventory where IM_NO =" + delno;

		String name = null;
		String I_MY = null;
		int IM_INT = 0;
		int I_INT = 0;
		try {
			ProcBoard.result = ProcBoard.st.executeQuery(SQL_item);
			while (ProcBoard.result.next()) {
				// 캐삭빵권 전용 옵션
				String cantdel = ProcBoard.result.getString("IM_NAME");
				if (cantdel.equals("캐삭빵 면제권")) {
					String updateCharinfo2 = String
							.format("update charinfo set CANTDEL = 0 where C_NAME = '" + Data.Main_C_NAME + "';");
					ProcBoard.st.executeUpdate(updateCharinfo2); // 수정된 부분
				}
				////////////////////////////////////////////////////////////////

				name = ProcBoard.result.getString("IM_NAME");
				I_MY = ProcBoard.result.getString("IM_MY");
				IM_INT = ProcBoard.result.getInt("IM_INT");
				int price = ProcBoard.result.getInt("IM_MONEY");

				if (Data.x == 0 && Data.y == 20) {
					price = (int) (price - (price * -8.8));
					ItemObject.itempay = price;
				} else {
					price = (int) (price - (price * 0.8));
					ItemObject.itempay = price;
				}

				System.out.println(itempay);
				sumCI = C_money + ItemObject.itempay;
				Material.line();
				System.out.println("기존 자산 : " + C_money + " G");
				System.out.println("획득한 자산 : " + ItemObject.itempay + " G");
				Material.line();
				System.out.println("현재 내 자산 : " + sumCI + " G");
			}

			String SQL_item6 = "select * from item where I_NAME ='" + name + "';";
			try (Statement newS = ProcBoard.con.createStatement()) {
				ResultSet newR = newS.executeQuery(SQL_item6);
				while (newR.next()) {
					I_INT = newR.getInt("I_INT");
				}
				if (I_INT == 1) { //정상확인
					// 아이템 삭제 처리
					String delnox2 = String.format("delete from item where" + "  I_NAME = '%s' and I_MY = '%s';", name,
							Data.Main_C_NAME);
//			String delnox2 = String.format("delete from item where I_FIELD = '"+ Data.Main_C_NAME+"'"
//					+ " and  I_NAME = '%s' and I_MY = '%s';", name, Data.Main_C_NAME);
					System.out.println(delnox2);
					try {
						ProcBoard.st.executeUpdate(delnox2);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

					String updateCharinfo = String.format(
							"update charinfo set C_MONEY = '%d' where C_NAME ='" + Data.Main_C_NAME + "';", sumCI);
					System.out.println(updateCharinfo);
					try {
						ProcBoard.st.executeUpdate(updateCharinfo);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {//정상확인
					// 아이템 갯수 삭제
					String delnox3 = String.format("update item set I_INT = I_INT-1 WHERE " + "I_NAME = '"
							+ name + "' and I_MY = '" + Data.Main_C_NAME + "';");
					System.out.println(delnox3);
					ProcBoard.dbExecuteUpdate2(delnox3, sumCI);

				}
			} catch (Exception e) {
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		System.out.println("판매 종료");
		ItemOP.shop();
	}

	static public void purchaseItem() {
		Store_item.public_item_load();
		Store_item.public_item();
		Material.line();
		Cw.Wn("[1] 자세히 보기 |  [2] 구매하기  | [SP] 뒤로가기");
		try {
			loop2: while (true) {
				int input1 = System.in.read(); // 현재 키보드 입력 값이 숫자로 변환된 상태
				boolean Sysread1 = input1 >= 97 && input1 <= 122;
				boolean Sysread21 = input1 >= 65 && input1 <= 90;
				if (Sysread1 == true || Sysread21 == true) {
					Cw.Wn("숫자만와 SP만 사용 가능합니다.");
					Cw.Wn("TIP 이동을 원할 시 뒤로가기를 누르세요");
				}
				if (input1 == 49) { // 1 자세히보기
					showItemDetails();
				} // "1"
				else if (input1 == 50) {// 2 구매하기
					PurchaseConfirmation();
				} // "2"
				if (input1 == 32) {
					System.out.println("구매 종료1");
					ItemOP.shop();
					break loop2;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 240112 정상작동확인
	static public void showItemDetails() {
		String cmd = InputS.r("상세하게 볼 아이템 번호");
		String SQL_item2 = "select * from stand where I_NO =" + cmd;
		try {
			ProcBoard.result = ProcBoard.st.executeQuery(SQL_item2);
			Material.line();

			if (!ProcBoard.result.next()) {
				System.out.println("해당 아이템이 없습니다.");
			} else {
				String title = ProcBoard.result.getString("I_NAME");
				String intno = ProcBoard.result.getString("I_INT");
				String content = ProcBoard.result.getString("I_TEXT");
				String price = ProcBoard.result.getString("I_MONEY");
				System.out.print("아이템명: " + title);
				System.out.println(" X " + intno);
				System.out.println("설명: " + content);
				System.out.println(price + " G");
				Material.line();
				Cw.Wn("[1] 자세히 보기 |  [2] 구매하기  | [SP] 뒤로가기");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			// 결과 집합을 닫아준다.
			if (ProcBoard.result != null) {
				try {
					ProcBoard.result.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 240112 정상작동확인
	static public void PurchaseConfirmation() { // 구매확정
		ResultSet newResult2 = null;
		Material.line();
		System.out.println("내 자산 : " + C_money + " G");
		System.out.println("*원하지 않을 시 0번을 입력하세요");
		String cmd = InputS.r("구매할 아이템 번호");

		String SQL_item2 = "SELECT * FROM stand WHERE I_NO =" + cmd;
		try (Statement NS = ProcBoard.con.createStatement()) {
			newResult2 = NS.executeQuery(SQL_item2);

			while (newResult2.next()) {
				// 캐삭빵권 전용 옵션
				String cantdel = newResult2.getString("I_NAME");
				if (cantdel.equals("캐삭빵 면제권")) {
					String updateCharinfo2 = String
							.format("update charinfo set CANTDEL = 1 where C_NAME = '" + Data.Main_C_NAME + "';");
					ProcBoard.st.executeUpdate(updateCharinfo2); // 수정된 부분
				}
				////////////////////////////////////////////////////////////////

				int I_M = newResult2.getInt("I_MONEY");
				sumCI = C_money - I_M;
				Material.line();
				System.out.println("기존 자산 : " + C_money + " G");
				System.out.println("소비한 자산 : " + I_M + " G");
				Material.line();
				System.out.println("현재 내 자산 : " + sumCI + " G");

				String updateCharinfo = String.format("UPDATE charinfo SET C_MONEY = '%d' WHERE C_NAME ='%s';", sumCI,
						Data.Main_C_NAME);
//                System.out.println(updateCharinfo);
				ProcBoard.st.executeUpdate(updateCharinfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 명시적으로 ResultSet을 닫음
			if (newResult2 != null) {
				try {
					newResult2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// 아이템 인벤토리로 이동
		Inventory.moveItemToInventory(cmd, sumCI);

		System.out.println("구매 종료");
		ItemOP.shop();
	}

}

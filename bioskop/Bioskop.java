import java.util.Scanner;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class Bioskop {
	Scanner input = new Scanner(System.in);
	String[][] kumpulanKursi = {
		{"KOSONG", "KOSONG", "KOSONG", "KOSONG", "KOSONG", "KOSONG", "TERISI", "TERISI"},
		{"TERISI", "KOSONG", "KOSONG", "TERISI", "KOSONG", "KOSONG", "KOSONG", "KOSONG"},
		{"KOSONG", "KOSONG", "KOSONG", "KOSONG", "TERISI", "KOSONG", "TERISI", "KOSONG"},
		{"KOSONG", "KOSONG", "KOSONG", "TERISI", "KOSONG", "TERISI", "KOSONG", "KOSONG"},
		{"TERISI", "KOSONG", "KOSONG", "TERISI", "KOSONG", "TERISI", "TERISI", "TERISI"}
	};
	String[] kumpulanFilm = {
		"Beck", "Fatman", "Honest Thief", "The Silence", "Toys of Terror",
		"Cosmoball", "Peninsula", "The Scientist", "Alone", "Frozen II"
	};
	Map<String, String> detailPesanan = new HashMap<String, String>();

	public final static void clearConsole() {
	    try {
	        if (System.getProperty("os.name").contains("Windows")) {
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        }
	        else {
	            System.out.print("\033\143");
	        }
	    } 
	    catch (final Exception e) {
	    	System.out.println("Invalid!");
	    }
	}

	void tampilFilm () {
		int urutan = 0;

		clearConsole();
		System.out.println( ansi().bold().a("\n LIST FILM\n").reset() );

		for (String film : kumpulanFilm) {
			urutan++;
			System.out.println(" " + urutan + ". " + film);
		}

		System.out.println();
	}

	void tampilKetersediaanKursi() {
		int urutanBarisY = 0;

		System.out.println( ansi().bold().a("\n KETERSEDIAAN KURSI PENONTON").reset() );

		System.out.println(
			"\n KETERANGAN \n" +
			ansi().fg(RED).a(" KOSONG").reset()   + " : " + "Belum diisi." + "\n" +
			ansi().fg(GREEN).a(" TERISI").reset() + " : " + "Sudah diisi." + "\n" +
			ansi().fg(BLUE).a(" -KAMU-").reset()  + " : " + "Kursi yang kamu pesan." + "\n"
		);
		
		System.out.println();
		for (int i = 0; i < kumpulanKursi.length; i++) {
			for(int j = 0; j < kumpulanKursi[i].length; j++)  System.out.print("\t  " + ( j + 1 ));
			break;
		}
		System.out.println("\n");

		for (String[] baris : kumpulanKursi) {
			urutanBarisY++;
			System.out.print("\n " + urutanBarisY);
			for (String kursi : baris) {
				if (kursi.equals("KOSONG")) System.out.print( 
					ansi().fg(RED).a("\t" + kursi).reset() + " "
				);
				else if (kursi.equals("TERISI")) System.out.print(
				 	ansi().fg(GREEN).a("\t" + kursi).reset() + " " 
				);
				else if (kursi.equals("-KAMU-")) System.out.print(
					ansi().fg(BLUE).a("\t" + kursi).reset() + " " 
				);
			}
			System.out.println("\n");
		}
	}

	void tampilDetailPesanan () {
		System.out.println(" ==========================\n");
		System.out.println("  DETAIL PESANAN \n");
		for (Map.Entry<String, String> el : detailPesanan.entrySet()) {
			System.out.println("  " + el.getKey() + " : " + el.getValue());
		}
		System.out.println("\n ==========================");
	}

	void pesanTiketFilm () {
		String pesanan;

		System.out.print(
			" Masukkan nomor film yang ingin anda tonton." +
			"\n ( Input berupa nomor film. Tulis exit jika tidak ingin memesan tiket film. )" +
			"\n >> "
		);

		pesanan = input.nextLine();

		if (!pesanan.equals("exit")) {
			try {
				clearConsole();
				detailPesanan.put("Film", kumpulanFilm[Integer.parseInt(pesanan)-1]+"");
				detailPesanan.put("Kursi", "");
				tampilDetailPesanan();
				tampilKetersediaanKursi();
				bookKursi();
			} catch (Exception e) {
				clearConsole();
				tampilFilm();
				System.out.println("\n Invalid! Masukkan nomor film yang benar.");
				pesanTiketFilm();
			}
		} else {
			System.out.println( ansi().fg(GREEN).a("\n TERIMA KASIH, SILAHKAN DATANG DI LAIN HARI.\n").reset() );
		}
	}

	void bookKursi () {
		String pesanan; 
		int letakX, letakY;
		
		System.out.print(
			"\n Masukkan nomor kursi yang ingin dipesan." +
			"\n ( Input berupa baris x, baris y . Contohnya 2,4 . Tulis exit jika sudah selesai. )" +
			"\n >> "
		);

		pesanan = input.nextLine().replaceAll("\\s", "");
		
		if (!pesanan.equals("exit")) {
			try {
				letakX = Integer.parseInt(pesanan.split(",")[0]);
				letakY = Integer.parseInt(pesanan.split(",")[1]);
				clearConsole();
				if (kumpulanKursi[letakY-1][letakX-1].equals("TERISI")) {
					tampilDetailPesanan();
					tampilKetersediaanKursi();
					System.out.println("\n\n Maaf, Kursi tersebut telah diisi!");
					bookKursi();
				} else if (kumpulanKursi[letakY-1][letakX-1].equals("-KAMU-")) {
					tampilDetailPesanan();
					tampilKetersediaanKursi();
					System.out.println("\n\n Kursi tersebut sudah kamu pesan.");
					bookKursi();
				} else {
					kumpulanKursi[letakY-1][letakX-1] = "-KAMU-";
					detailPesanan.put("Kursi", detailPesanan.get("Kursi") + letakX + "-" + letakY + "  ");
					tampilDetailPesanan();
					tampilKetersediaanKursi();
					bookKursi();
				}
			} catch (Exception e) {
				clearConsole();
				tampilDetailPesanan();
				tampilKetersediaanKursi();
				System.out.println("\n\n Invalid! Masukkan nomor kursi dengan benar!");
				bookKursi();
			}	
		} else if (pesanan.equals("exit") && !detailPesanan.get("Kursi").equals("")) {
			clearConsole();
			tampilDetailPesanan();
			System.out.println( ansi().fg(GREEN).a("\n TERIMA KASIH, SELAMAT MENONTON.\n").reset() );
		} else {
			clearConsole();
			tampilDetailPesanan();
			tampilKetersediaanKursi();
			System.out.println("\n\n Pilih setidaknya 1 kursi.");
			bookKursi();
		}
	}

	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		Bioskop bioskop = new Bioskop();
		
		bioskop.tampilFilm();
		bioskop.pesanTiketFilm();
	}
}
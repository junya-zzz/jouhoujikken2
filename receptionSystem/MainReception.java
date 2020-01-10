package receptionSystem;




public class MainReception extends Thread{

	private Reception reception;

	public static void main(String[] args) {
		MainReception main= new MainReception();
		main.reception = new Reception();
		main.start();
		main.reception.sendLug();


	}

	public void run() {
		reception.getLug();
	}
}











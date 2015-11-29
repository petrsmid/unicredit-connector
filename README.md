![enter image description here](https://www.unicreditbank.cz/web/img/content/logo-u/t_UCBk-3D.jpg)
Connector to Unicredit API for Common Banking Tasks
============================================

*Note: Contact me if you need help. I am the author of the Unicredit API as well as author of this library.*

The internet banking of Unicredit offers the possibility to connect to its API 
from an external program and perform usual banking tasks. The feature is called [BusinessNet Connect](http://www.unicreditbank.sk/sk/Firmy/Cash-management/Elektronicke-bankovnictvo/Businessnet-professional) and is available in Unicredit SK, CZ, SI, UA, RO and RU. The feature is not publicly offered in all of the countries however the bank provides it if you ask for it. 

This library was tested against Unicredit SK.

It provides API for:

 - payments (domestic payments, foreign payments, sepa, ...)
 - reading actual ballance
 - reading payment history

This library supports payments and ballance. The payment history is not currently supported. (feel free to add the support)

The Unicredit API
=================

In order to use the Unicredit API one has to purchase this feature - see e.g. [here](http://www.unicreditbank.sk/sk/Firmy/Cash-management/Elektronicke-bankovnictvo/Businessnet-professional). It costs monthly around 8,- EUR. See the [price list](http://www.unicreditbank.sk/att/151087/14814_UCB_Cennik_SK_firmy_01072015_v2.pdf).

For a detailed information please read the [technical document](https://www.unicreditbank.cz/files/download/electronic_banking/BusinessNet_Connect_Integration_document.pdf) provided by the Unicredit bank. Here we provide only a brief description.

The Unicredit interface is a webdav folder (e.g. on address https://sk.unicreditbanking.net/webdav/) - one can mount it as a disk into Windows Explorer, mount it in Linux or open in [Total Commander](http://www.ghisler.com/) with [additional Webdav plugin](http://www.ghisler.com/plugins.htm) (this is the most reliable solution which I strongly recommend).

Payment
-----------
One can upload a file in a specific format containing list of payments (called "package") into a particular folder of the webdav (e.g. `/upload/foreign/Gemini_Format/My_Payment.txt`).
Later one can download a corresponding file (e.g. `/upload/status/My_Payment.txt`) with information about status of the package (currently processed, signed, error, ...).

The package must be signed by GPG otherwise the payments are not processed and must be signed manually in the internet banking (with SMS, certificate or anything else).

The certificate is provided to the customer by purchasing the BusinessNet Connect feature by request.
Additionally the package may be encrypted with public certificate of the Unicredit bank.
(Note: the encryption is not implemented by this library because the communication is performed over HTTPS which is a secure channel anyway.)

The payments are processed immediately after successfull upload of the signed package into the webdav folder.

Formats of the package
----------------------

Unicredit supports multiple formats of the package.
As the Unicredit is not using the same backend for each country the supported formats differ per country.
This library uses Multicash as it is the mostly supported format. 

Note: we tested this library against Unicredit SK. If you have account in different country you should check the supported formats. If the Multicash is not supported you can either contact
me or add the support to this library yourself.

Ballance
-----------
The actual ballance is provided in files in directory `/balances`. However this library uses (because of historical reasons) the balance found in the last transaction of the payment history. Feel free to fix it.

Payment History
---------------------
The payment history is provided in files in directory `/statements`.

Note: The payment history contains list of transactions since one day before the actual day. If you need real time information you would have to purchase the service [MT942](http://www.unicreditbank.sk/sk/Firmy/Cash-management/Elektronicke-bankovnictvo/Businessnet-professional).


Usage
=======

See examples of the usage in `Examples.java` file.

The Payment
----------------

	//start the connector
	UnicreditConnector connector = buildConnector();
		
	//create payment
	ForeignPayment payment = preparePayment(); 

	//Pay!
	connector.uploadForeignPaymentPackage("ref_12345", payment);		

What happens under the cover: The connector creates a package file with one payment. The file would be named TODO. The file is signed by PGP and uploaded into the webdav folder.


The Ballance
-------------------

	//start the connector
	UnicreditConnector connector = buildConnector();
		
	BigDecimal balance = connector.getLastBalance();
	//if balance > 1.000.000 =>  you are rich!

What happens under the cover: The connector reads balance from the last statement file. This is because of historical reasons, nowadays it would be probably easier to read it from `/balances`. (Feel free to fix it.) 

The information is not up to date (it is one day behind). If you need realtime information please perform the fix described above or purchase the service [MT942] provided by the Unicredit and read the data from the "realtime" file.

Configuration
-----------------

TODO - fix the code!!!

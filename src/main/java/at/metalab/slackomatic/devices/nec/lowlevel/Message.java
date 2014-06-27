package at.metalab.slackomatic.devices.nec.lowlevel;

import java.io.UnsupportedEncodingException;

/**
 * Class for creating the NEC lowlevel packets.
 * 
 * @author m68k
 * 
 */
public abstract class Message {

	private final byte type;

	private final byte destination;

	private byte[] payload;

	/**
	 * Those Magic constants are defined by NEC
	 * 
	 * @author m68k
	 * 
	 */
	private static class MagicConstants {

		/**
		 * Constants used in the packet header
		 * 
		 * @author m68k
		 * 
		 */
		private static class Header {

			/**
			 * Constants used as the indicator which type of message follows the
			 * header in the message block.
			 * 
			 * @author m68k
			 * 
			 */
			private static class MessageTypes {
				/**
				 * Command Message. Sent from the controller to a device.
				 */
				private final static byte COMMAND = toAscii('A');

				/**
				 * Response to a Command Message. Sent from a device to the
				 * controller.
				 */
				private final static byte COMMAND_REPLY = toAscii('B');

				/**
				 * Get Parameter Message. Sent from the controller to a device.
				 */
				private final static byte GET_PARAMETER = toAscii('C');

				/**
				 * Response to a Get Parameter Message. Sent from a device to
				 * the controller.
				 */
				private final static byte GET_PARAMETER_REPLY = toAscii('D');

				/**
				 * Set Parameter Message. Sent from the controller to a device.
				 */
				private final static byte SET_PARAMETER = toAscii('E');

				/**
				 * Response to a Set Parameter Message. Sent from a device to
				 * the controller.
				 */
				private final static byte SET_PARAMETER_REPLY = toAscii('F');
			}

			/**
			 * Reserved by NEC for future enhancements. Has to be set to '0' for
			 * now.
			 */
			private final static byte RESERVED = toAscii('0');

			/**
			 * Has to be set as the source of packets which are sent to the
			 * devices.
			 */
			private final static byte CONTROLLER_ID = toAscii('0');
		}

		/**
		 * Delimiters between the various parts of the data packet.
		 * 
		 * @author m68k
		 * 
		 */
		private static class Delimiters {
			/**
			 * Start of header
			 */
			private final static byte SOH = 0x1;

			/**
			 * Start of message ("Start of text" in ASCII)
			 */
			private final static byte STX = 0x2;

			/**
			 * End of message ("End of text" in ASCII)
			 */
			private final static byte ETX = 0x3;

			/**
			 * Delimiter used at the end of the raw data stream of each message.
			 * Called 'Delimiter' and 'End of packet' in the NEC documentation.
			 */
			private final static byte END_OF_PACKET = 0xD;
		}

	}

	protected Message(byte type, byte destination) {
		this.type = type;
		this.destination = destination;
	}

	protected Message(byte type) {
		this.type = type;
		this.destination = toAscii('0');
	}

	public static class CommandMessage extends Message {

		public CommandMessage(char destination, String command) {
			super(MagicConstants.Header.MessageTypes.COMMAND,
					toAscii(destination));
			setPayload(toAscii(command));
		}

	}

	public static class CommandReplyMessage extends Message {
		public CommandReplyMessage() {
			super(MagicConstants.Header.MessageTypes.COMMAND_REPLY);
		}
	}

	public static class GetParameterMessage extends Message {
		public GetParameterMessage(char destination) {
			super(MagicConstants.Header.MessageTypes.GET_PARAMETER,
					toAscii(destination));
		}
	}

	public static class GetParameterReplyMessage extends Message {
		public GetParameterReplyMessage() {
			super(MagicConstants.Header.MessageTypes.GET_PARAMETER_REPLY);
		}
	}

	public static class SetParameterMessage extends Message {

		public SetParameterMessage(char destination, short opCodePage,
				short opCode, int value) {
			super(MagicConstants.Header.MessageTypes.SET_PARAMETER,
					toAscii(destination));

			String xOpCodePage = toZeroPaddedHex(2, opCodePage);
			String xOpCode = toZeroPaddedHex(2, opCode);
			String xValue = toZeroPaddedHex(4, value);

			setPayload(toAscii(String.format("%s%s%s", xOpCodePage, xOpCode,
					xValue)));
		}
	}

	public static class SetParameterReplyMessage extends Message {
		public SetParameterReplyMessage() {
			super(MagicConstants.Header.MessageTypes.SET_PARAMETER_REPLY);
		}
	}

	public static CommandMessage createCommand(char destination, String payload) {
		return new CommandMessage(destination, payload);
	}

	public static CommandReplyMessage createCommandReply() {
		return new CommandReplyMessage();
	}

	public static GetParameterMessage createGetParameter(char destination) {
		return new GetParameterMessage(destination);
	}

	public static GetParameterReplyMessage createGetParameterReply() {
		return new GetParameterReplyMessage();
	}

	public static SetParameterMessage createSetParameter(char destination,
			int opCodePage, int opCode, int value) {
		return new SetParameterMessage(destination, (short) opCodePage,
				(short) opCode, value);
	}

	public static SetParameterReplyMessage createSetParameterReply() {
		return new SetParameterReplyMessage();
	}

	protected void setPayload(byte[] payload) {
		this.payload = payload;
	}

	private byte[] getPayload() {
		return payload;
	}

	/**
	 * Create the header block
	 * (SOH|RESERVED|$DESTINATION|CONTROLLER_ID|$TYPE|$LEN)
	 * 
	 * @param message
	 * @return
	 */
	private byte[] createHeaderBlockData(byte[] message) {
		String xMsgLen = toZeroPaddedHex(2, message.length);

		return new byte[] { MagicConstants.Delimiters.SOH,
				MagicConstants.Header.RESERVED, destination,
				MagicConstants.Header.CONTROLLER_ID, type,
				toAscii(xMsgLen.charAt(0)), toAscii(xMsgLen.charAt(1)) };
	}

	/**
	 * Create the message block (STX|$PAYLOAD|ETX)
	 * 
	 * @return
	 */
	private byte[] createMessageBlockData() {
		byte[] payload = getPayload();
		byte[] messageBlock = new byte[payload.length + 2];

		messageBlock[0] = MagicConstants.Delimiters.STX;

		for (int i = 0; i < payload.length; i++) {
			messageBlock[i + 1] = payload[i];
		}

		messageBlock[messageBlock.length - 1] = MagicConstants.Delimiters.ETX;

		return messageBlock;
	}

	/**
	 * Create the Block check code byte value for the data.
	 * 
	 * @param combinedData
	 * @return
	 */
	private byte calculateBlockCheckCode(byte[] combinedData) {
		byte bcc = combinedData[1]; // initial value of the bcc is the second
									// byte

		// skip the first byte (SOH) and the last two (as those are not set yet
		// and not part of the bcc)
		for (int i = 2; i < combinedData.length - 2; i++) {
			bcc = (byte) (bcc ^ combinedData[i]); // xor all the bytes
		}

		return bcc;
	}

	/**
	 * Create the packet ($HEADER|$MESSAGE|$BCC|END_OF_PACKET)
	 * 
	 * @return
	 */
	public byte[] createPacket() {
		final byte[] message = createMessageBlockData();
		final int messageLen = message.length;

		final byte[] header = createHeaderBlockData(message);
		final int headerLen = header.length;

		// 2 extra bytes, one for the BCC and one for the delimiter at the end
		byte[] packet = new byte[headerLen + messageLen + 2];

		{
			// combine all the stuff into the packet byte array which will be
			// returned to the caller
			int targetIdx = 0;

			// copy the message header
			for (int i = 0; i < headerLen; i++) {
				packet[targetIdx] = header[i];
				targetIdx++;
			}

			// copy the message payload
			for (int i = 0; i < messageLen; i++) {
				packet[targetIdx] = message[i];
				targetIdx++;
			}

			// calculate and set the bcc
			packet[targetIdx] = calculateBlockCheckCode(packet);
			targetIdx++;

			// add the end of message delimiter at the end
			packet[targetIdx] = MagicConstants.Delimiters.END_OF_PACKET;
		}

		return packet;
	}

	/**
	 * Returns the value in hexadecimal as a String (padded with left zeros to
	 * the specified length).
	 * 
	 * @param len
	 * @param value
	 * @return
	 */
	private static String toZeroPaddedHex(int len, int value) {
		String pattern = String.format("%%0%dX", len);
		String hex = String.format(pattern, value);

		return hex;
	}

	/**
	 * Return the character encoded in US-ASCII as a byte.
	 * 
	 * @param c
	 * @return
	 */
	private static byte toAscii(char c) {
		return toAscii(String.valueOf(c))[0];
	}

	/**
	 * Return the String encoded in US-ASCII as a byte array.
	 * 
	 * @param s
	 * @return
	 */
	private static byte[] toAscii(String s) {
		try {
			return s.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException exception) {
			throw new RuntimeException("Could not convert to US-ASCII charset",
					exception);
		}
	}
}

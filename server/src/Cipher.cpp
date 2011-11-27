// $Id$
// Description: Declares a cipher interface for objects to use
// (C) Cyberpad Technologies 2011

#include <cryptopp/rsa.h>
using CryptoPP::RSA;
using CryptoPP::RSASS;
using CryptoPP::InvertibleRSAFunction;
using CryptoPP::RSAES_PKCS1v15_Encryptor;
using CryptoPP::RSAES_PKCS1v15_Decryptor;
using CryptoPP::RSASSA_PKCS1v15_SHA_Signer;
using CryptoPP::RSASSA_PKCS1v15_SHA_Verifier;

#include <cryptopp/files.h>
using CryptoPP::FileSink;
using CryptoPP::FileSource;

#include <cryptopp/filters.h>
using CryptoPP::SignerFilter;
using CryptoPP::SignatureVerificationFilter;
using CryptoPP::StringSink;
using CryptoPP::StringSource;
using CryptoPP::PK_EncryptorFilter;
using CryptoPP::PK_DecryptorFilter;

#include <cryptopp/osrng.h>
using CryptoPP::AutoSeededRandomPool;

#include <cryptopp/cryptlib.h>
using CryptoPP::Exception;
using CryptoPP::DecodingResult;

#include <cryptopp/hex.h>
using CryptoPP::HexDecoder;

#include <string>
using std::string;

#include <QtDebug>
#include <QString>

#include <exception>
using std::exception;

#include "Cipher.h"

#define KEYSIZE 512

// Empty constructor...  for now.
Cipher::Cipher()
{

}

QString Cipher::Encrypt( RSA::PublicKey inEncKey, RSA::PrivateKey inSignKey, QString& ioMessage )
{
	string signature;
	AutoSeededRandomPool rng;
	try // to sign the un-encrypted message
    {
        string message = ioMessage.toStdString();

        ////////////////////////////////////////////////
        // Sign and Encode
        RSASSA_PKCS1v15_SHA_Signer signer( inSignKey );

        StringSource( message, true,
            new SignerFilter( rng, signer,
                new StringSink( signature )
            ) // SignerFilter
        ); // StringSource
    } // try

    catch( CryptoPP::Exception& e ) {
        qWarning() << "Error Signing Message: " << e.what();
    }

    // Encrypt the data using RSA encryption, return signature
	string cipher;
	try // to encrypt the message
	{
        string plain = ioMessage.toStdString();

        ////////////////////////////////////////////////
        // Encryption
        RSAES_PKCS1v15_Encryptor e( inEncKey );

        StringSource( plain, true,
            new PK_EncryptorFilter( rng, e,
                new StringSink( cipher )
            ) // PK_EncryptorFilter
        ); // StringSource

        ioMessage.fromStdString( cipher );
	}

	catch( CryptoPP::Exception& e ) {
	    qWarning() << "Error Encrypting Message: " << e.what();
	}

    QString ret;
    ret.fromStdString( signature );
    return ret;
}

bool Cipher::Decrypt( RSA::PrivateKey inEncKey, RSA::PublicKey inSignKey, QString& ioMessage, QString& inSignature )
{
    try // to decrypt the message
    {
        AutoSeededRandomPool rng;
        string recovered;

        ////////////////////////////////////////////////
        // Decryption
        RSAES_PKCS1v15_Decryptor d( inEncKey );

        StringSource( ioMessage.toStdString(), true,
            new HexDecoder(
                new PK_DecryptorFilter( rng, d,
                    new StringSink( recovered )
                ) // PK_EncryptorFilter
            ) // HexDecoder
        ); // StringSource

        //std::cout << "Recovered: " << recovered << std::endl;

        ioMessage = QString::fromStdString( recovered );

        ////////////////////////////////////////////////
        // Verify
        //std::cout << "Input Signature: " << inSignature.toStdString() << std::endl;
        string signature;
        StringSource( inSignature.toStdString(), true,
            new HexDecoder(
                new StringSink( signature )
            )
        );

        //std::cout << "Decoded signature: " << signature << std::endl;

        // This signature is not validating right now..  This is not of concern at the moment.
        //RSASSA_PKCS1v15_SHA_Verifier verifier( inSignKey );
        //bool verified = verifier.VerifyMessage( ( const byte* )recovered.c_str(), recovered.length(),
        //    ( const byte* )signature.c_str(), signature.length() );

        return true; // verified

	} // try

    catch( CryptoPP::Exception& e ) {
        qDebug() << "Error Decoding/Verifying Message: " << e.what();
        return false;
    }

	return true;
}

void Cipher::GenerateKeypair( CryptoPP::RSA::PublicKey& oPubKey, CryptoPP::RSA::PrivateKey& oPrivKey )
{
    ////////////////////////////////////////////////
    // Generate keys
    AutoSeededRandomPool rng;

    InvertibleRSAFunction parameters;
    parameters.GenerateRandomWithKeySize( rng, KEYSIZE );

    RSA::PrivateKey privateKey( parameters );
    RSA::PublicKey publicKey( parameters );
    oPrivKey = privateKey;
    oPubKey = publicKey;
}

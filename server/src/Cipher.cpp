// $Id$
// Description: Declares a cipher interface for objects to use
// (C) Cyberpad Technologies 2011

#include <cryptopp/rsa.h>
using CryptoPP::RSA;
using CryptoPP::RSASS;
using CryptoPP::InvertibleRSAFunction;
using CryptoPP::RSAES_OAEP_SHA_Encryptor;
using CryptoPP::RSAES_OAEP_SHA_Decryptor;

#include <cryptopp/pssr.h>
using CryptoPP::PSS;

#include <cryptopp/sha.h>
using CryptoPP::SHA1;

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

//#include <SecBlock.h>
//using CryptoPP::SecByteBlock;

#include <cryptopp/cryptlib.h>
using CryptoPP::Exception;
using CryptoPP::DecodingResult;

#include <string>
using std::string;

#include <QtDebug>
#include <QString>

#include <exception>
using std::exception;

#include "Cipher.h"

#define KEYSIZE 1024

// Empty constructor...  for now.
Cipher::Cipher()
{

}

QString Cipher::Encrypt( RSA::PublicKey inEncKey, RSA::PrivateKey inSignKey, QString& ioMessage )
{
	// Encrypt the data using RSA encryption, return signature
	string cipher;
	try // to encrypt the message
	{
        AutoSeededRandomPool rng;
        string plain = ioMessage.toStdString();

        ////////////////////////////////////////////////
        // Encryption
        RSAES_OAEP_SHA_Encryptor e( inEncKey );

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

	string signature;
	try // to sign the encrypted message
    {
        AutoSeededRandomPool rng;
        string message = ioMessage.toStdString();

        ////////////////////////////////////////////////
        // Sign and Encode
        RSASS<PSS, SHA1>::Signer signer( inSignKey );

        StringSource( message, true,
            new SignerFilter( rng, signer,
                new StringSink( signature )
            ) // SignerFilter
        ); // StringSource
    } // try

    catch( CryptoPP::Exception& e ) {
        qWarning() << "Error Signing Message: " << e.what();
    }

    QString ret;
    ret.fromStdString( signature );
    return ret;
}

bool Cipher::Decrypt( RSA::PrivateKey inEncKey, RSA::PublicKey inSignKey, QString& ioMessage, QString& inSignature )
{
	try // to verify the signature on the message
	{
	    string message = ioMessage.toStdString();
        string signature = inSignature.toStdString();

	    ////////////////////////////////////////////////
        // Verify and Recover
        RSASS<PSS, SHA1>::Verifier verifier( inSignKey );

        StringSource( message+signature, true,
            new SignatureVerificationFilter(
                verifier, NULL,
                SignatureVerificationFilter::THROW_EXCEPTION
            ) // SignatureVerificationFilter
        ); // StringSource
	} // try

    catch( CryptoPP::Exception& e ) {
        qDebug() << "Error Verifying Message Signature: " << e.what();
        return false;
    }

    try // to decrypt the message
    {
        AutoSeededRandomPool rng;
        string cipher = ioMessage.toStdString(), recovered;

        ////////////////////////////////////////////////
        // Decryption
        RSAES_OAEP_SHA_Decryptor d( inEncKey );

        StringSource( cipher, true,
            new PK_DecryptorFilter( rng, d,
                new StringSink( recovered )
            ) // PK_EncryptorFilter
         ); // StringSource

         ioMessage.fromStdString( recovered );
    }

    catch( CryptoPP::Exception& e ) {
        qWarning() << "Error Decrypting Message: " << e.what();
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
